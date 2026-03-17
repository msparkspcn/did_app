package com.secta9ine.didapp.data.repository

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
        data class Error(val reason: String) : DeviceAccess
    }

    suspend fun resolveDeviceAccess(deviceId: String): DeviceAccess {
        logger.i(TAG, "resolveDeviceAccess() deviceId=$deviceId")
        return try {
            val response = api.getDeviceAuth(deviceId)
            val authStatus = response.data?.authStatus
            logger.i(TAG, "getDeviceAuth() response: authStatus=$authStatus deviceId=${response.data?.deviceId}")

            when (authStatus) {
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
        } catch (e: Exception) {
            logger.e(TAG, "Failed to check device auth: ${e.message}", e)
            DeviceAccess.Error("서버 연결 실패: ${e.message}")
        }
    }

    private suspend fun registerDevice(deviceId: String): DeviceAccess {
        logger.i(TAG, "registerDevice() deviceId=$deviceId")
        return try {
            val request = DeviceRegisterRequest(
                deviceId = deviceId,
                serialNumber = deviceId,
                model = android.os.Build.MODEL,
                appVersion = "1.0.0"
            )
            val response = api.registerDevice(request)
            val data = response.data

            if (data != null) {
                tokenManager.saveDeviceId(data.deviceId)
                tokenManager.saveApprovalCode(data.authenticationCode ?: "")
                logger.i(TAG, "Device registered: deviceId=${data.deviceId}, authStatus=${data.authStatus}, code=${data.authenticationCode}")
                DeviceAccess.PendingApproval(data.authenticationCode ?: "")
            } else {
                logger.w(TAG, "Register response data is null: ${response.message}")
                DeviceAccess.Error("디바이스 등록 실패: ${response.message}")
            }
        } catch (e: Exception) {
            logger.e(TAG, "Failed to register device: ${e.message}", e)
            DeviceAccess.Error("디바이스 등록 실패: ${e.message}")
        }
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
                if (event is DeviceEvent.Activated) {
                    tokenManager.saveDeviceId(event.data.deviceId)
                    logger.i(TAG, "DeviceId saved: ${event.data.deviceId}, authStatus=${event.data.authStatus}")
                }
                onEvent(event)
            }
        )
    }

    fun unsubscribeDeviceEvents() {
        logger.i(TAG, "unsubscribeDeviceEvents()")
        deviceEventWebSocket.disconnect()
    }
}
