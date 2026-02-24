package com.secta9ine.didapp.v2.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.secta9ine.didapp.v2.contract.AssetDto
import com.secta9ine.didapp.v2.contract.AssetType
import com.secta9ine.didapp.v2.contract.CanvasDto
import com.secta9ine.didapp.v2.contract.CoordinateSystem
import com.secta9ine.didapp.v2.contract.FitMode
import com.secta9ine.didapp.v2.contract.LayoutDto
import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
import com.secta9ine.didapp.v2.contract.TransitionType
import com.secta9ine.didapp.v2.contract.ZoneDto
import com.secta9ine.didapp.v2.contract.ZonePlaylistItemDto
import com.secta9ine.didapp.v2.data.local.V2AssetEntity
import com.secta9ine.didapp.v2.data.local.V2SnapshotDao
import com.secta9ine.didapp.v2.data.local.V2SnapshotEntity
import com.secta9ine.didapp.v2.data.local.V2ZoneEntity
import com.secta9ine.didapp.v2.data.local.V2ZonePlaylistItemEntity
import com.secta9ine.didapp.v2.data.remote.SnapshotWebSocketClient
import com.secta9ine.didapp.v2.data.remote.V2PlayerApi
import com.secta9ine.didapp.v2.mock.MockSnapshots
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DidV2Repository @Inject constructor(
    private val api: V2PlayerApi,
    private val dao: V2SnapshotDao,
    private val gson: Gson,
    private val wsClient: SnapshotWebSocketClient
) {
//    private val wsUrl = "ws://10.0.2.2:8080/ws/player-snapshot"
    private val wsUrl = "ws://10.212.44.212:8080/ws/player-snapshot"
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

    suspend fun syncInitialSnapshot(didId: String) {
        runCatching { api.getPlayerSnapshot(didId) }
            .onSuccess { replaceSnapshot(it) }
            .onFailure {
                // No backend yet: keep app visible with a seeded snapshot.
                if (!dao.hasSnapshot()) {
                    replaceSnapshot(MockSnapshots.threeZoneDemo())
                }
            }
    }

    fun startRealtime(didId: String, scope: CoroutineScope) {
        wsClient.connect(wsUrl = wsUrl, didId = didId) { snapshot ->
            scope.launch {
                replaceSnapshot(snapshot)
            }
        }
    }

    fun stopRealtime() {
        wsClient.disconnect()
    }

    private suspend fun replaceSnapshot(snapshot: PlayerSnapshotDto) {
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
        val assetEntities = snapshot.assets.values.map {
            V2AssetEntity(
                assetId = it.id,
                type = it.type.name,
                source = it.source,
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
    }

    private fun parseMetadata(metadataJson: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return runCatching { gson.fromJson<Map<String, String>>(metadataJson, type) }.getOrDefault(emptyMap())
    }

    private inline fun <reified T : Enum<T>> enumValueOfOrDefault(raw: String, default: T): T {
        return runCatching { enumValueOf<T>(raw) }.getOrDefault(default)
    }
}
