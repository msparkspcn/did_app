package com.secta9ine.didapp.data.repository

import com.secta9ine.didapp.data.local.DeviceDao
import com.secta9ine.didapp.data.local.DeviceEntity
import com.secta9ine.didapp.data.remote.DeviceEvent
import com.secta9ine.didapp.data.remote.DeviceEventWebSocket
import com.secta9ine.didapp.data.remote.DeviceRegisterRequest
import com.secta9ine.didapp.data.remote.DidApi
import com.secta9ine.didapp.system.FileLogger
import com.secta9ine.didapp.system.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DidRepository @Inject constructor(
    private val api: DidApi,
    private val tokenManager: TokenManager,
    private val deviceDao: DeviceDao,
    private val deviceEventWebSocket: DeviceEventWebSocket,
    private val logger: FileLogger
) {
    companion object {
        private const val TAG = "DidRepository"
        private const val WS_BASE_URL = "ws://10.120.44.88:14000/ws/devices"
    }

    sealed interface DeviceAccess {
        data class Active(val deviceId: String) : DeviceAccess
        data class PendingApproval(val approvalCode: String) : DeviceAccess
        /** 이전에 인증된 디바이스이나 서버 통신 실패 → 오프라인 모드 */
        data class OfflineActive(val deviceId: String) : DeviceAccess
        /** 인증 이력 없고 서버 통신 실패 */
        data class ServerUnreachable(val reason: String) : DeviceAccess
        data class Error(val reason: String) : DeviceAccess
    }

    /**
     * 디바이스 인증 상태 확인.
     *
     * 1. 서버 통신 성공 → 서버 상태 기준으로 판단, 로컬 DB 동기화
     * 2. 서버 통신 실패
     *    - 로컬에 ACTIVE 이력 있음 → OfflineActive (오프라인 모드)
     *    - 로컬에 이력 없음 → ServerUnreachable (서버 연결 필요)
     */
    suspend fun resolveDeviceAccess(deviceId: String): DeviceAccess {
        logger.i(TAG, "resolveDeviceAccess() deviceId=$deviceId")

        val localDevice = deviceDao.getDevice(deviceId)
        logger.d(TAG, "Local device: ${localDevice?.authStatus ?: "NOT_FOUND"}")

        return try {
            val access = resolveFromServer(deviceId)

            // 서버 응답 성공 시 로컬 DB 동기화
            when (access) {
                is DeviceAccess.Active -> {
                    deviceDao.saveDevice(
                        DeviceEntity(
                            deviceId = access.deviceId,
                            authStatus = "ACTIVE",
                            lastSyncedAt = System.currentTimeMillis()
                        )
                    )
                    logger.d(TAG, "Local device synced as ACTIVE")
                }
                is DeviceAccess.PendingApproval -> {
                    deviceDao.saveDevice(
                        DeviceEntity(
                            deviceId = deviceId,
                            authStatus = "PENDING_APPROVAL",
                            authenticationCode = access.approvalCode,
                            lastSyncedAt = System.currentTimeMillis()
                        )
                    )
                    logger.d(TAG, "Local device synced as PENDING_APPROVAL")
                }
                else -> {}
            }

            access
        } catch (e: Exception) {
            logger.e(TAG, "Server unreachable: ${e.message}", e)

            // 서버 통신 실패 → 로컬 이력 확인
            if (localDevice != null && localDevice.authStatus == "ACTIVE") {
                logger.i(TAG, "Fallback to offline mode (previously ACTIVE)")
                DeviceAccess.OfflineActive(localDevice.deviceId)
            } else {
                logger.w(TAG, "No local auth history, server required")
                DeviceAccess.ServerUnreachable("서버에 연결할 수 없습니다.\n네트워크 연결을 확인해주세요.")
            }
        }
    }

    /**
     * 서버에서 인증 상태를 조회하고, NOT_FOUND이면 자동 등록한다.
     */
    private suspend fun resolveFromServer(deviceId: String): DeviceAccess {
        val response = api.getDeviceAuth(deviceId)
        val authStatus = response.data?.authStatus
        logger.i(TAG, "getDeviceAuth() response: authStatus=$authStatus")

        return when (authStatus) {
            "ACTIVE" -> {
                val id = response.data.deviceId ?: deviceId
                tokenManager.saveDeviceId(id)
                logger.i(TAG, "Device is ACTIVE: $id")
                DeviceAccess.Active(id)
            }
            "PENDING_APPROVAL" -> {
                val code = response.data.authenticationCode ?: ""
                tokenManager.saveApprovalCode(code)
                logger.i(TAG, "Device is PENDING_APPROVAL, code=$code")
                DeviceAccess.PendingApproval(code)
            }
            "NOT_FOUND", null -> {
                logger.i(TAG, "Device NOT_FOUND, registering...")
                registerDevice(deviceId)
            }
            else -> {
                logger.w(TAG, "Unknown authStatus: $authStatus")
                DeviceAccess.Error("알 수 없는 디바이스 상태: $authStatus")
            }
        }
    }

    private suspend fun registerDevice(deviceId: String): DeviceAccess {
        logger.i(TAG, "registerDevice() deviceId=$deviceId")
        val request = DeviceRegisterRequest(
            deviceId = deviceId,
            serialNumber = deviceId,
            model = android.os.Build.MODEL,
            appVersion = "1.0.0"
        )
        val response = api.registerDevice(request)
        val data = response.data

        return if (data != null) {
            tokenManager.saveDeviceId(data.deviceId)
            tokenManager.saveApprovalCode(data.authenticationCode ?: "")

            deviceDao.saveDevice(
                DeviceEntity(
                    deviceId = data.deviceId,
                    authStatus = data.authStatus,
                    authenticationCode = data.authenticationCode,
                    storeId = data.storeId,
                    deviceName = data.deviceName,
                    lastSyncedAt = System.currentTimeMillis()
                )
            )

            logger.i(TAG, "Device registered: deviceId=${data.deviceId}, code=${data.authenticationCode}")
            DeviceAccess.PendingApproval(data.authenticationCode ?: "")
        } else {
            logger.w(TAG, "Register response data is null: ${response.message}")
            DeviceAccess.Error("디바이스 등록 실패: ${response.message}")
        }
    }

    /**
     * DEVICE_ACTIVATED 이벤트 수신 시 로컬 DB를 ACTIVE로 갱신한다.
     */
    suspend fun onDeviceActivated(event: DeviceEvent.Activated) {
        tokenManager.saveDeviceId(event.data.deviceId)
        deviceDao.saveDevice(
            DeviceEntity(
                deviceId = event.data.deviceId,
                authStatus = "ACTIVE",
                authenticationCode = event.data.authenticationCode,
                storeId = event.data.storeId,
                activatedAt = event.data.approvedAt,
                lastSyncedAt = System.currentTimeMillis()
            )
        )
        logger.i(TAG, "Device activated and saved locally: ${event.data.deviceId}")
    }

    fun subscribeDeviceEvents(
        deviceId: String,
        onEvent: (DeviceEvent) -> Unit
    ) {
        logger.i(TAG, "subscribeDeviceEvents() deviceId=$deviceId wsUrl=$WS_BASE_URL")
        deviceEventWebSocket.connect(
            wsUrl = WS_BASE_URL,
            deviceId = deviceId,
            onEvent = { event ->
                logger.i(TAG, "DeviceEvent received: ${event::class.simpleName} deviceId=${event.deviceId}")
                onEvent(event)
            }
        )
    }

    fun unsubscribeDeviceEvents() {
        logger.i(TAG, "unsubscribeDeviceEvents()")
        deviceEventWebSocket.disconnect()
    }
}
