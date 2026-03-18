package com.secta9ine.didapp.system

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import net.quber.qubersignageagent.IQuberCallback
import net.quber.qubersignageagent.IQuberManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.resume

class QuberAgentManager(
    private val context: Context,
    private val gson: Gson,
    private val logger: FileLogger
) {
    companion object {
        private const val TAG = "QuberAgent"
        private const val AGENT_PACKAGE = "net.quber.qubersignageagent"
        private const val AGENT_ACTION = "net.quber.qubersignageagent.QUBER_AGENT_SERVICE"
        private const val TIMEOUT_MS = 10_000L
    }

    private var aidl: IQuberManager? = null
    private var bound = false
    private val pendingResponses = ConcurrentHashMap<String, CompletableDeferred<JsonObject>>()

    private val callback = object : IQuberCallback.Stub() {
        override fun responseListener(jsonMsg: String) {
            logger.d(TAG, "Response: $jsonMsg")
            try {
                val json = gson.fromJson(jsonMsg, JsonObject::class.java)
                val responseId = json.get("responseId")?.asString
                if (responseId != null) {
                    pendingResponses.remove(responseId)?.complete(json)
                }
            } catch (e: Exception) {
                logger.e(TAG, "Failed to parse response: ${e.message}")
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            aidl = IQuberManager.Stub.asInterface(service)
            bound = true
            logger.i(TAG, "Service connected")

            aidl?.let { manager ->
                try {
                    manager.multiAgentResponse(context.packageName, callback)
                    logger.i(TAG, "Callback registered (Multi Binder)")
                } catch (e: Exception) {
                    logger.e(TAG, "Failed to register callback: ${e.message}")
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            aidl = null
            bound = false
            logger.w(TAG, "Service disconnected")
            // 재연결 시도
            bind()
        }
    }

    fun bind() {
        if (bound) return
        val intent = Intent(AGENT_ACTION).apply {
            setPackage(AGENT_PACKAGE)
        }
        try {
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            logger.i(TAG, "Binding to Quber Agent...")
        } catch (e: Exception) {
            logger.e(TAG, "Failed to bind: ${e.message}")
        }
    }

    fun unbind() {
        if (!bound) return
        try {
            aidl?.multiClose(context.packageName)
        } catch (e: Exception) {
            Log.w(TAG, "multiClose failed: ${e.message}")
        }
        try {
            context.unbindService(serviceConnection)
        } catch (e: Exception) {
            Log.w(TAG, "unbindService failed: ${e.message}")
        }
        aidl = null
        bound = false
        logger.i(TAG, "Unbound from Quber Agent")
    }

    val isConnected: Boolean get() = bound && aidl != null

    private fun generateRequestId(): String {
        return SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(Date())
    }

    /**
     * Send a command and wait for response (suspend, with timeout).
     */
    suspend fun sendCommand(cmdCode: String, params: JsonElement? = null): QuberResponse {
        val manager = aidl ?: return QuberResponse.Error("Not connected to Quber Agent")

        val requestId = generateRequestId()
        val request = JsonObject().apply {
            addProperty("requestId", requestId)
            addProperty("cmdCode", cmdCode)
            if (params != null) {
                add("params", params)
            }
        }

        val deferred = CompletableDeferred<JsonObject>()
        pendingResponses[requestId] = deferred

        val jsonMsg = gson.toJson(request)
        logger.d(TAG, "Sending: $jsonMsg")

        try {
            val sent = manager.multiSendRequestCmd(context.packageName, jsonMsg)
            if (!sent) {
                pendingResponses.remove(requestId)
                return QuberResponse.Error("sendRequestCmd returned false")
            }

            val response = withTimeout(TIMEOUT_MS) { deferred.await() }
            val resultCode = response.get("resultCode")?.asString ?: "unknown"
            return if (resultCode == "2000") {
                QuberResponse.Success(resultCode, response)
            } else {
                QuberResponse.Failure(resultCode, response)
            }
        } catch (e: TimeoutCancellationException) {
            pendingResponses.remove(requestId)
            return QuberResponse.Error("Timeout waiting for response (cmdCode=$cmdCode)")
        } catch (e: Exception) {
            pendingResponses.remove(requestId)
            return QuberResponse.Error("${e.javaClass.simpleName}: ${e.message}")
        }
    }

    /**
     * Fire-and-forget command (no response wait).
     */
    fun sendCommandAsync(cmdCode: String, params: JsonObject? = null): Boolean {
        val manager = aidl ?: run {
            logger.e(TAG, "Not connected, cannot send cmdCode=$cmdCode")
            return false
        }

        val requestId = generateRequestId()
        val request = JsonObject().apply {
            addProperty("requestId", requestId)
            addProperty("cmdCode", cmdCode)
            if (params != null) {
                add("params", params)
            }
        }

        val jsonMsg = gson.toJson(request)
        logger.d(TAG, "Sending (async): $jsonMsg")

        return try {
            manager.multiSendRequestCmd(context.packageName, jsonMsg)
        } catch (e: Exception) {
            logger.e(TAG, "sendCommandAsync failed: ${e.message}")
            false
        }
    }

    // ── Convenience methods for common commands ──

    /** Reboot the STB */
    fun reboot(): Boolean = sendCommandAsync(CmdCode.REBOOT)

    /** Read firmware version */
    suspend fun readFirmwareVersion(): QuberResponse = sendCommand(CmdCode.FIRMWARE_VERSION_READ)

    /** Read CPU/Memory info */
    suspend fun readSystemInfo(): QuberResponse = sendCommand(CmdCode.CPU_MEMORY_READ)

    /** Read Device ID */
    suspend fun readDeviceId(): QuberResponse = sendCommand(CmdCode.DEVICE_ID_READ)

    /** Set Device ID */
    suspend fun setDeviceId(deviceId: String): QuberResponse {
        val params = JsonObject().apply { addProperty("deviceId", deviceId) }
        return sendCommand(CmdCode.DEVICE_ID_SET, params)
    }

    /** Read display resolution */
    suspend fun readDisplayResolution(): QuberResponse = sendCommand(CmdCode.DISPLAY_RESOLUTION_READ)

    /** Read schedule */
    suspend fun readSchedule(): QuberResponse = sendCommand(CmdCode.SCHEDULE_READ)

    /** Set schedule */
    suspend fun setSchedule(scheduleParams: JsonObject): QuberResponse =
        sendCommand(CmdCode.SCHEDULE_SET, scheduleParams)

    /** Read STB display status */
    suspend fun readDisplayStatus(): QuberResponse = sendCommand(CmdCode.DISPLAY_STATUS_READ)

    /** HDMI ON/OFF */
    suspend fun setHdmiOnOff(onOff: String): QuberResponse {
        val params = JsonObject().apply { addProperty("hdmiOnOff", onOff) }
        return sendCommand(CmdCode.HDMI_ON_OFF_SET, params)
    }

    /** Navigation bar show/hide */
    fun showNavigationBar(): Boolean = sendCommandAsync(CmdCode.NAV_BAR_SHOW)
    fun hideNavigationBar(): Boolean = sendCommandAsync(CmdCode.NAV_BAR_HIDE)

    /** AutoRun setting */
    suspend fun setAutoRun(packageName: String, className: String): QuberResponse {
        val params = JsonObject().apply {
            addProperty("packageName", packageName)
            addProperty("className", className)
        }
        return sendCommand(CmdCode.AUTORUN_SET, params)
    }

    /** Read AutoRun setting */
    suspend fun readAutoRun(): QuberResponse = sendCommand(CmdCode.AUTORUN_READ)

    /** System Sleep Mode */
    suspend fun setSleepMode(mode: String): QuberResponse {
        val params = JsonObject().apply { addProperty("sleepMode", mode) }
        return sendCommand(CmdCode.SLEEP_MODE_SET, params)
    }

    /** Display brightness */
    suspend fun setBrightness(value: Int): QuberResponse {
        val params = JsonObject().apply { addProperty("brightness", value.toString()) }
        return sendCommand(CmdCode.BRIGHTNESS_SET, params)
    }

    /** Display rotation */
    suspend fun setDisplayRotation(angle: Int): QuberResponse {
        val params = JsonObject().apply { addProperty("rotateDegree", angle) }
        return sendCommand(CmdCode.DISPLAY_ROTATION_SET, params)
    }

    object CmdCode {
        // STB
        const val FIRMWARE_VERSION_READ = "211006"
        const val CPU_MEMORY_READ = "211008"
        const val REBOOT = "215001"
        const val APP_RESET = "215029"
        const val NETWORK_RESET = "215030"
        const val FACTORY_RESET = "215002"

        // Device ID
        const val DEVICE_ID_READ = "211036"
        const val DEVICE_ID_SET = "213022"
        const val DEVICE_ID_RESET = "215023"

        // ADB
        const val ADB_READ = "211001"
        const val ADB_SET = "213001"

        // Display
        const val DISPLAY_RESOLUTION_LIST = "211005"
        const val DISPLAY_RESOLUTION_READ = "211002"
        const val DISPLAY_RESOLUTION_SET = "213002"
        const val DISPLAY_ROTATION_READ = "211003"
        const val DISPLAY_ROTATION_SET = "213003"
        const val DISPLAY_STATUS_READ = "211023"
        const val DISPLAY_DEVICE_READ = "111009"

        // Schedule
        const val SCHEDULE_READ = "211004"
        const val SCHEDULE_SET = "213004"

        // Brightness
        const val BRIGHTNESS_READ = "211007"
        const val BRIGHTNESS_SET = "213005"

        // Navigation Bar
        const val NAV_BAR_SHOW = "215006"
        const val NAV_BAR_HIDE = "215007"

        // HDMI
        const val HDMI_ON_OFF_SET = "213020"
        const val HDMI_CEC_READ = "211025"
        const val HDMI_CEC_SET = "213014"
        const val HDMI_CABLE_STATUS = "211024"

        // HDMI CEC Auto Power
        const val HDMI_CEC_AUTO_OFF_READ = "211026"
        const val HDMI_CEC_AUTO_OFF_SET = "213015"
        const val HDMI_CEC_AUTO_ON_READ = "211027"
        const val HDMI_CEC_AUTO_ON_SET = "213016"
        const val HDMI_CEC_POWER_STATUS_READ = "211049"
        const val HDMI_CEC_POWER_STATUS_SET = "215031"

        // Sleep Mode
        const val SLEEP_MODE_READ = "211028"
        const val SLEEP_MODE_SET = "213017"

        // AutoRun
        const val AUTORUN_READ = "211034"
        const val AUTORUN_SET = "213019"
        const val AUTORUN_DELETE = "214002"

        // Network
        const val ETHERNET_MAC_READ = "211012"
        const val WIFI_STATUS_READ = "211013"
        const val NETWORK_TYPE_READ = "211022"

        // App
        const val APP_LIST_READ = "211033"
        const val APP_INSTALL = "215021"
    }
}

sealed class QuberResponse {
    data class Success(val resultCode: String, val data: JsonObject) : QuberResponse()
    data class Failure(val resultCode: String, val data: JsonObject) : QuberResponse()
    data class Error(val message: String) : QuberResponse()

    val isSuccess: Boolean get() = this is Success

    fun getParams(): JsonObject? = when (this) {
        is Success -> data.getAsJsonObject("params")
        is Failure -> data.getAsJsonObject("params")
        is Error -> null
    }
}
