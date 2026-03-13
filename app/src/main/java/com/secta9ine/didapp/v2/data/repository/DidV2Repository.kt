package com.secta9ine.didapp.v2.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.secta9ine.didapp.v2.contract.AssetDto
import com.secta9ine.didapp.v2.contract.AssetType
import com.secta9ine.didapp.v2.contract.CanvasDto
import com.secta9ine.didapp.v2.contract.CoordinateSystem
import com.secta9ine.didapp.v2.contract.FitMode
import com.secta9ine.didapp.v2.contract.LayoutDto
import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
import com.secta9ine.didapp.v2.contract.PowerScheduleDto
import com.secta9ine.didapp.v2.contract.TransitionType
import com.secta9ine.didapp.v2.contract.ZoneDto
import com.secta9ine.didapp.v2.contract.ZonePlaylistItemDto
import com.secta9ine.didapp.v2.data.local.V2AssetEntity
import com.secta9ine.didapp.v2.data.local.V2SnapshotDao
import com.secta9ine.didapp.v2.data.local.V2SnapshotEntity
import com.secta9ine.didapp.v2.data.local.V2ZoneEntity
import com.secta9ine.didapp.v2.data.local.V2ZonePlaylistItemEntity
import com.secta9ine.didapp.v2.data.remote.DeviceRegisterRequestDto
import com.secta9ine.didapp.v2.data.remote.SnapshotWebSocketClient
import com.secta9ine.didapp.v2.data.remote.V2PlayerApi
import com.secta9ine.didapp.v2.mock.MockSnapshots
import com.secta9ine.didapp.system.PowerScheduleManager
import com.secta9ine.didapp.util.AssetDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.URI
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DidV2Repository @Inject constructor(
    private val api: V2PlayerApi,
    private val dao: V2SnapshotDao,
    private val gson: Gson,
    private val wsClient: SnapshotWebSocketClient,
    private val downloader: AssetDownloader,
    private val powerScheduleManager: PowerScheduleManager
) {
    sealed interface DeviceAccess {
        object Active : DeviceAccess
        object PendingApproval : DeviceAccess
        data class Blocked(val status: String) : DeviceAccess
        data class Error(val reason: String) : DeviceAccess
    }

//    private val wsUrl = "ws://10.0.2.2:8080/ws/player-snapshot"
    private val wsUrl = "ws://10.212.44.212:8080/ws/player-snapshot"
    private val devJwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkaWQtMDAxIiwicm9sZSI6ImRldiJ9.mTWi_MeRhODeQ382jeLB26y2rTgE-kyqOIbovUjKUAM"

    private val _isSleeping = MutableStateFlow(false)
    val isSleeping: StateFlow<Boolean> = _isSleeping
    private var sleepCheckJob: Job? = null
    val snapshotFlow: Flow<PlayerSnapshotDto?> = combine(
        dao.observeSnapshot(),
        dao.observeZones(),
        dao.observeAssets(),
        dao.observeZonePlaylistItems()
    ) { snapshot, zones, assets, zoneItems ->
        if (snapshot == null) {
            null
        } else {
            val assetsMap = assets.associate { entity ->
                entity.assetId to AssetDto(
                    id = entity.assetId,
                    type = enumValueOfOrDefault(entity.type, AssetType.IMAGE),
                    source = entity.source,
                    localPath = entity.localPath,
                    metadata = parseMetadata(entity.metadataJson),
                    defaultDurationSec = entity.defaultDurationSec
                )
            }
            val playlists = zoneItems.groupBy { it.zoneId }.mapValues { (_, list) ->
                list.sortedBy { it.playOrder }.map {
                    ZonePlaylistItemDto(
                        assetId = it.assetId,
                        order = it.playOrder,
                        durationSec = it.durationSec,
                        transition = enumValueOfOrDefault(it.transition, TransitionType.NONE)
                    )
                }
            }
            PlayerSnapshotDto(
                version = snapshot.version,
                validFromEpochSec = snapshot.validFromEpochSec,
                validToEpochSec = snapshot.validToEpochSec,
                layout = LayoutDto(
                    id = snapshot.layoutId,
                    canvas = CanvasDto(snapshot.canvasWidth, snapshot.canvasHeight),
                    coordinateSystem = enumValueOfOrDefault(snapshot.coordinateSystem, CoordinateSystem.CANVAS_PIXEL),
                    zones = zones.map {
                        ZoneDto(
                            id = it.zoneId,
                            x = it.x,
                            y = it.y,
                            width = it.width,
                            height = it.height,
                            zIndex = it.zIndex,
                            backgroundHex = it.backgroundHex,
                            fitMode = enumValueOfOrDefault(it.fitMode, FitMode.COVER)
                        )
                    }
                ),
                zonePlaylists = playlists,
                assets = assetsMap
            )
        }
    }

    suspend fun resolveDeviceAccess(didId: String): DeviceAccess {
        val statusResponse = runCatching { api.getDeviceStatus(didId) }
            .onFailure { error ->
                Log.w("DidV2Repository", "Device status API failed didId=$didId reason=${error.message}", error)
            }
            .getOrElse { return DeviceAccess.Error("DEVICE_STATUS_API_FAILED") }
        val status = normalizeStatus(statusResponse.status)
        Log.i("DidV2Repository", "Device status didId=$didId status=$status")
        return when (status) {
            "ACTIVE" -> DeviceAccess.Active
            "PENDING_APPROVAL" -> DeviceAccess.PendingApproval
            "SUSPENDED", "REVOKED" -> DeviceAccess.Blocked(status)
            "NOT_FOUND" -> registerDeviceAndResolve(didId)
            else -> DeviceAccess.Error("UNKNOWN_DEVICE_STATUS_$status")
        }
    }

    suspend fun syncInitialSnapshot(didId: String) {
        runCatching { api.getPlayerSnapshot(didId) }
            .onSuccess { snapshot ->
                Log.i("DidV2Repository", "Initial snapshot API success didId=$didId version=${snapshot.version}")
                applySnapshotIfNew(snapshot, channel = "API")
            }
            .onFailure { error ->
                Log.w(
                    "DidV2Repository",
                    "Initial snapshot API failed didId=$didId reason=${error.message}",
                    error
                )
                // No backend yet: keep app visible with a seeded snapshot.
                if (!dao.hasSnapshot()) {
                    Log.i("DidV2Repository", "Fallback to local mock snapshot didId=$didId")
                    replaceSnapshot(MockSnapshots.threeZoneDemo())
                } else {
                    Log.i("DidV2Repository", "Keep existing local snapshot didId=$didId")
                }
            }
    }

    suspend fun syncPowerSchedule(didId: String) {
        runCatching { api.getPowerSchedule(didId) }
            .onSuccess { schedule ->
                Log.i("DidV2Repository", "Power schedule fetched didId=$didId enabled=${schedule.enabled} off=${schedule.powerOffTime} on=${schedule.powerOnTime}")
                powerScheduleManager.applySchedule(schedule)
                _isSleeping.value = powerScheduleManager.isInSleepWindow()
            }
            .onFailure { error ->
                Log.w("DidV2Repository", "Power schedule API failed didId=$didId reason=${error.message}", error)
                _isSleeping.value = powerScheduleManager.isInSleepWindow()
            }
    }

    fun startSleepCheckLoop(scope: CoroutineScope) {
        sleepCheckJob?.cancel()
        sleepCheckJob = scope.launch {
            while (isActive) {
                val shouldSleep = powerScheduleManager.isInSleepWindow()
                if (_isSleeping.value != shouldSleep) {
                    Log.i("DidV2Repository", "Sleep state changed: $shouldSleep")
                    _isSleeping.value = shouldSleep
                }
                delay(30_000L) // check every 30 seconds
            }
        }
    }

    fun updateSleepState(sleeping: Boolean) {
        _isSleeping.value = sleeping
    }

    fun startRealtime(didId: String, scope: CoroutineScope) {
        wsClient.connect(
            wsUrl = wsUrl,
            didId = didId,
            jwtToken = devJwtToken,
            onSnapshot = { snapshot ->
                scope.launch {
                    val applied = applySnapshotIfNew(snapshot, channel = "WS")
                    if (applied) {
                        Log.d("DidV2Repository", "WS snapshot applied didId=$didId version=${snapshot.version}")
                    }
                }
            },
            onPowerSchedule = { schedule ->
                scope.launch {
                    Log.i("DidV2Repository", "WS power schedule update: enabled=${schedule.enabled} off=${schedule.powerOffTime} on=${schedule.powerOnTime}")
                    powerScheduleManager.applySchedule(schedule)
                    _isSleeping.value = powerScheduleManager.isInSleepWindow()
                }
            }
        )
    }

    fun stopRealtime() {
        wsClient.disconnect()
    }

    private suspend fun registerDeviceAndResolve(didId: String): DeviceAccess {
        val registerResponse = runCatching {
            api.registerDevice(DeviceRegisterRequestDto(didId = didId))
        }.onFailure { error ->
            Log.w("DidV2Repository", "Device register API failed didId=$didId reason=${error.message}", error)
        }.getOrElse { return DeviceAccess.Error("DEVICE_REGISTER_API_FAILED") }

        val registeredStatus = normalizeStatus(registerResponse.status)
        Log.i("DidV2Repository", "Device register response didId=$didId status=$registeredStatus")
        return when (registeredStatus) {
            "ACTIVE" -> DeviceAccess.Active
            "PENDING_APPROVAL", "NOT_FOUND" -> DeviceAccess.PendingApproval
            "SUSPENDED", "REVOKED" -> DeviceAccess.Blocked(registeredStatus)
            else -> DeviceAccess.Error("UNKNOWN_REGISTER_STATUS_$registeredStatus")
        }
    }

    private suspend fun replaceSnapshot(snapshot: PlayerSnapshotDto): List<V2AssetEntity> {
        val existingLocalPaths = dao.getAssets().mapNotNull { it.localPath }.toSet()
        val referencedAssetIds = snapshot.zonePlaylists.values
            .flatten()
            .map { it.assetId }
            .toSet()
        val snapshotEntity = V2SnapshotEntity(
            version = snapshot.version,
            validFromEpochSec = snapshot.validFromEpochSec,
            validToEpochSec = snapshot.validToEpochSec,
            layoutId = snapshot.layout.id,
            canvasWidth = snapshot.layout.canvas.width,
            canvasHeight = snapshot.layout.canvas.height,
            coordinateSystem = snapshot.layout.coordinateSystem.name
        )
        val zoneEntities = snapshot.layout.zones.map {
            V2ZoneEntity(
                zoneId = it.id,
                x = it.x,
                y = it.y,
                width = it.width,
                height = it.height,
                zIndex = it.zIndex,
                backgroundHex = it.backgroundHex,
                fitMode = it.fitMode.name
            )
        }
        val assetEntities = referencedAssetIds.mapNotNull { assetId -> snapshot.assets[assetId] }.map {
            val localPath = when (it.type) {
                AssetType.IMAGE, AssetType.VIDEO -> {
                    if (isValidRemoteUrl(it.source)) {
                        downloader.downloadFile(it.source, buildAssetFileName(it))
                    } else {
                        Log.w("DidV2Repository", "Skip invalid asset source: id=${it.id}, source=${it.source}")
                        null
                    }
                }
                AssetType.TEXT, AssetType.PRODUCT -> null
            }
            V2AssetEntity(
                assetId = it.id,
                type = it.type.name,
                source = it.source,
                localPath = localPath,
                metadataJson = gson.toJson(it.metadata),
                defaultDurationSec = it.defaultDurationSec
            )
        }
        val zonePlaylistEntities = snapshot.zonePlaylists.flatMap { (zoneId, items) ->
            items.map {
                V2ZonePlaylistItemEntity(
                    zoneId = zoneId,
                    assetId = it.assetId,
                    playOrder = it.order,
                    durationSec = it.durationSec,
                    transition = it.transition.name
                )
            }
        }
        dao.replaceSnapshot(snapshotEntity, zoneEntities, assetEntities, zonePlaylistEntities)
        val currentLocalPaths = assetEntities.mapNotNull { it.localPath }.toSet()
        (existingLocalPaths - currentLocalPaths).forEach { downloader.deleteFile(it) }
        return assetEntities
    }

    private suspend fun applySnapshotIfNew(
        snapshot: PlayerSnapshotDto,
        channel: String
    ): Boolean {
        val currentVersion = dao.getSnapshotVersion()
        if (currentVersion != null) {
            if (snapshot.version == currentVersion) {
                Log.i(
                    "DidV2Repository",
                    "$channel snapshot skipped (same version) version=${snapshot.version}"
                )
                return false
            }
            if (snapshot.version < currentVersion) {
                Log.w(
                    "DidV2Repository",
                    "$channel snapshot skipped (stale) incoming=${snapshot.version}, local=$currentVersion"
                )
                return false
            }
        }
        val processedAssets = replaceSnapshot(snapshot)
        val localPathSummary = processedAssets.joinToString(", ") { asset ->
            "${asset.assetId}=${asset.localPath ?: "null"}"
        }
        Log.d(
            "DidV2Repository",
            "$channel snapshot persisted version=${snapshot.version} localPaths=[$localPathSummary]"
        )
        return true
    }

    private fun parseMetadata(metadataJson: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return runCatching { gson.fromJson<Map<String, String>>(metadataJson, type) }.getOrDefault(emptyMap())
    }

    private inline fun <reified T : Enum<T>> enumValueOfOrDefault(raw: String, default: T): T {
        return runCatching { enumValueOf<T>(raw) }.getOrDefault(default)
    }

    private fun buildAssetFileName(asset: AssetDto): String {
        val rawName = runCatching {
            val path = URI(asset.source).path.orEmpty()
            path.substringAfterLast('/').ifBlank { "asset" }
        }.getOrDefault("asset")
        val safeName = rawName.replace(Regex("[^A-Za-z0-9._-]"), "_")
        val hash = asset.source.hashCode().toUInt().toString(16)
        return "${hash}_$safeName"
    }

    private fun isValidRemoteUrl(raw: String): Boolean {
        if (raw.isBlank()) return false
        return runCatching {
            val url = URL(raw)
            (url.protocol == "http" || url.protocol == "https") && !url.host.isNullOrBlank()
        }.getOrDefault(false)
    }

    private fun normalizeStatus(raw: String): String {
        return raw.trim().uppercase()
    }
}
