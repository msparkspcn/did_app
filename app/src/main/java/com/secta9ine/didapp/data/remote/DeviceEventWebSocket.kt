package com.secta9ine.didapp.data.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.secta9ine.didapp.data.remote.stomp.StompClient
import com.secta9ine.didapp.system.FileLogger
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

// ── 이벤트 타입 ──

enum class DeviceEventType {
    DEVICE_ACTIVATED,
    SCREEN_UPDATED,
    PRODUCT_UPDATED,
    QUEUE_CALLED,
    RESTART,
    UPDATE,
    POWER_ON,
    POWER_OFF
}

// ── 이벤트 페이로드 DTO (서버 data 필드 기준) ──

data class DeviceActivatedData(
    val deviceId: String,
    val authStatus: String? = null,
    val authenticationCode: String? = null,
    val active: Boolean = false,
    val id: Long? = null,
    val storeId: Long? = null,
    val playlistId: String? = null,
    val contentId: String? = null,
    val approvedAt: String? = null
)

data class ScreenUpdatedData(
    val screenId: String? = null,
    val layoutType: String? = null
)

data class ProductUpdatedData(
    val productId: String? = null
)

data class QueueCalledData(
    val queueNumber: Int,
    val counterName: String? = null
)

data class UpdateData(
    val version: String,
    val downloadUrl: String
)

/**
 * 서버 이벤트 공통 envelope:
 * {
 *   "eventType": "DEVICE_ACTIVATED",
 *   "deviceId": "...",
 *   "occurredAt": "...",
 *   "data": { ... }
 * }
 */
sealed interface DeviceEvent {
    val deviceId: String
    val occurredAt: String?

    data class Activated(
        override val deviceId: String,
        override val occurredAt: String?,
        val data: DeviceActivatedData
    ) : DeviceEvent

    data class ScreenUpdated(
        override val deviceId: String,
        override val occurredAt: String?,
        val data: ScreenUpdatedData
    ) : DeviceEvent

    data class ProductUpdated(
        override val deviceId: String,
        override val occurredAt: String?,
        val data: ProductUpdatedData
    ) : DeviceEvent

    data class QueueCalled(
        override val deviceId: String,
        override val occurredAt: String?,
        val data: QueueCalledData
    ) : DeviceEvent

    data class Restart(
        override val deviceId: String,
        override val occurredAt: String?
    ) : DeviceEvent

    data class Update(
        override val deviceId: String,
        override val occurredAt: String?,
        val data: UpdateData
    ) : DeviceEvent

    data class PowerOn(
        override val deviceId: String,
        override val occurredAt: String?
    ) : DeviceEvent

    data class PowerOff(
        override val deviceId: String,
        override val occurredAt: String?
    ) : DeviceEvent
}

@Singleton
class DeviceEventWebSocket @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    private val logger: FileLogger
) {
    companion object {
        private const val TAG = "DeviceEventWS"
    }

    private var stompClient: StompClient? = null
    private var subscriptionId: String? = null

    fun connect(
        wsUrl: String,
        deviceId: String,
        onEvent: (DeviceEvent) -> Unit
    ) {
        disconnect()
        logger.i(TAG, "connect() wsUrl=$wsUrl deviceId=$deviceId")

        val client = StompClient(okHttpClient, logger)
        stompClient = client

        subscriptionId = client.subscribe("/topic/devices/$deviceId") { frame ->
            val body = frame.body
            if (body.isNullOrBlank()) return@subscribe

            logger.d(TAG, "Raw message body: $body")
            parseEvent(body)?.let { event ->
                logger.i(TAG, "Event parsed: ${event::class.simpleName}")
                onEvent(event)
            }
        }

        client.connect(url = wsUrl)
    }

    fun disconnect() {
        stompClient?.let { client ->
            subscriptionId?.let { client.unsubscribe(it) }
            client.disconnect()
        }
        stompClient = null
        subscriptionId = null
        logger.d(TAG, "disconnect()")
    }

    private fun parseEvent(body: String): DeviceEvent? {
        return runCatching {
            val json = gson.fromJson(body, JsonObject::class.java)
            val eventTypeStr = json.get("eventType")?.asString ?: return@runCatching null
            val eventType = runCatching { DeviceEventType.valueOf(eventTypeStr) }.getOrNull()
                ?: run {
                    logger.w(TAG, "Unknown eventType: $eventTypeStr")
                    return@runCatching null
                }
            val deviceId = json.get("deviceId")?.asString ?: ""
            val occurredAt = json.get("occurredAt")?.asString
            val data = json.get("data")

            when (eventType) {
                DeviceEventType.DEVICE_ACTIVATED -> {
                    val d = gson.fromJson(data, DeviceActivatedData::class.java)
                    DeviceEvent.Activated(deviceId, occurredAt, d)
                }
                DeviceEventType.SCREEN_UPDATED -> {
                    val d = gson.fromJson(data, ScreenUpdatedData::class.java)
                    DeviceEvent.ScreenUpdated(deviceId, occurredAt, d)
                }
                DeviceEventType.PRODUCT_UPDATED -> {
                    val d = gson.fromJson(data, ProductUpdatedData::class.java)
                    DeviceEvent.ProductUpdated(deviceId, occurredAt, d)
                }
                DeviceEventType.QUEUE_CALLED -> {
                    val d = gson.fromJson(data, QueueCalledData::class.java)
                    DeviceEvent.QueueCalled(deviceId, occurredAt, d)
                }
                DeviceEventType.RESTART -> DeviceEvent.Restart(deviceId, occurredAt)
                DeviceEventType.UPDATE -> {
                    val d = gson.fromJson(data, UpdateData::class.java)
                    DeviceEvent.Update(deviceId, occurredAt, d)
                }
                DeviceEventType.POWER_ON -> DeviceEvent.PowerOn(deviceId, occurredAt)
                DeviceEventType.POWER_OFF -> DeviceEvent.PowerOff(deviceId, occurredAt)
            }
        }.onFailure { e ->
            logger.e(TAG, "Failed to parse event: ${e.message}", e)
        }.getOrNull()
    }
}
