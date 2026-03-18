package com.secta9ine.didapp.data.remote.stomp

import com.secta9ine.didapp.system.FileLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.atomic.AtomicInteger

enum class StompConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

data class StompSubscription(
    val id: String,
    val destination: String,
    val callback: (StompFrame) -> Unit
)

class StompClient(
    private val okHttpClient: OkHttpClient,
    private val logger: FileLogger
) {
    companion object {
        private const val TAG = "StompClient"
        private const val STOMP_VERSION = "1.2"
        private const val MAX_RECONNECT_DELAY_MS = 30_000L
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var webSocket: WebSocket? = null
    private var wsUrl: String? = null
    private var connectHeaders: Map<String, String> = emptyMap()

    private val _connectionState = MutableStateFlow(StompConnectionState.DISCONNECTED)
    val connectionState: StateFlow<StompConnectionState> = _connectionState

    private val _errorFrames = MutableSharedFlow<StompFrame>(extraBufferCapacity = 8)
    val errorFrames: SharedFlow<StompFrame> = _errorFrames

    private val subscriptionIdCounter = AtomicInteger(0)
    private val subscriptions = mutableMapOf<String, StompSubscription>()

    private var reconnectJob: Job? = null
    private var reconnectAttempts = 0
    private var shouldReconnect = false

    private var heartbeatJob: Job? = null
    private var serverHeartbeatMs: Long = 0
    private var clientHeartbeatMs: Long = 0

    fun connect(
        url: String,
        headers: Map<String, String> = emptyMap()
    ) {
        // 기존 WebSocket만 정리 (구독 목록은 유지)
        closeWebSocket()
        reconnectJob?.cancel()

        shouldReconnect = true
        wsUrl = url
        connectHeaders = headers
        reconnectAttempts = 0
        logger.i(TAG, "connect() url=$url subscriptions=${subscriptions.size}")
        connectInternal()
    }

    fun disconnect() {
        shouldReconnect = false
        reconnectJob?.cancel()
        heartbeatJob?.cancel()

        if (_connectionState.value == StompConnectionState.CONNECTED) {
            sendFrame(StompFrame(StompCommand.DISCONNECT, mapOf("receipt" to "disconnect-receipt")))
        }

        webSocket?.close(1000, "STOMP disconnect")
        webSocket = null
        subscriptions.clear()
        _connectionState.value = StompConnectionState.DISCONNECTED
        logger.d(TAG, "disconnect() completed, subscriptions cleared")
    }

    /**
     * WebSocket만 닫고 구독 목록은 유지한다.
     * 재연결 시 resubscribeAll()로 자동 복원된다.
     */
    private fun closeWebSocket() {
        heartbeatJob?.cancel()
        webSocket?.close(1000, "closing for reconnect")
        webSocket = null
    }

    fun subscribe(
        destination: String,
        callback: (StompFrame) -> Unit
    ): String {
        val subId = "sub-${subscriptionIdCounter.getAndIncrement()}"
        val subscription = StompSubscription(subId, destination, callback)
        subscriptions[subId] = subscription

        if (_connectionState.value == StompConnectionState.CONNECTED) {
            sendSubscribeFrame(subscription)
        }

        logger.d(TAG, "subscribe() id=$subId destination=$destination")
        return subId
    }

    fun unsubscribe(subscriptionId: String) {
        subscriptions.remove(subscriptionId)?.let { sub ->
            if (_connectionState.value == StompConnectionState.CONNECTED) {
                sendFrame(
                    StompFrame(
                        StompCommand.UNSUBSCRIBE,
                        mapOf("id" to sub.id)
                    )
                )
            }
            logger.d(TAG, "unsubscribe() id=${sub.id} destination=${sub.destination}")
        }
    }

    fun send(
        destination: String,
        body: String? = null,
        headers: Map<String, String> = emptyMap()
    ) {
        val frameHeaders = mutableMapOf("destination" to destination)
        frameHeaders.putAll(headers)
        if (body != null) {
            frameHeaders["content-type"] = "application/json"
            frameHeaders["content-length"] = body.toByteArray(Charsets.UTF_8).size.toString()
        }
        logger.d(TAG, "send() destination=$destination bodyLength=${body?.length ?: 0}")
        sendFrame(StompFrame(StompCommand.SEND, frameHeaders, body))
    }

    // ── Internal ──────────────────────────────────────────

    private fun connectInternal() {
        val url = wsUrl ?: return
        _connectionState.value = StompConnectionState.CONNECTING
        logger.d(TAG, "connectInternal() url=$url")

        val request = Request.Builder().url(url).build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                logger.i(TAG, "WebSocket opened, sending STOMP CONNECT")
                val headers = mutableMapOf(
                    "accept-version" to STOMP_VERSION,
                    "heart-beat" to "10000,10000"
                )
                headers.putAll(connectHeaders)
                sendFrame(StompFrame(StompCommand.CONNECT, headers))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleFrame(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                logger.e(TAG, "WebSocket failure: ${t.message}", t)
                closeWebSocket()
                _connectionState.value = StompConnectionState.ERROR
                scheduleReconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                logger.w(TAG, "WebSocket closed: code=$code reason=$reason")
                closeWebSocket()
                _connectionState.value = StompConnectionState.DISCONNECTED
                scheduleReconnect()
            }
        })
    }

    private fun handleFrame(raw: String) {
        val frame = StompFrame.decode(raw) ?: return

        when (frame.command) {
            StompCommand.CONNECTED -> {
                reconnectAttempts = 0
                _connectionState.value = StompConnectionState.CONNECTED
                logger.i(TAG, "STOMP CONNECTED: version=${frame.headers["version"]}")
                negotiateHeartbeat(frame)
                resubscribeAll()
            }

            StompCommand.MESSAGE -> {
                val subId = frame.headers["subscription"]
                val destination = frame.headers["destination"]
                logger.d(TAG, "MESSAGE received: subscription=$subId destination=$destination bodyLength=${frame.body?.length ?: 0}")

                val subscription = subId?.let { subscriptions[it] }
                if (subscription != null) {
                    subscription.callback(frame)
                } else {
                    logger.w(TAG, "No subscription found for id=$subId")
                }
            }

            StompCommand.RECEIPT -> {
                logger.d(TAG, "RECEIPT: id=${frame.headers["receipt-id"]}")
            }

            StompCommand.ERROR -> {
                logger.e(TAG, "STOMP ERROR: ${frame.headers["message"]} body=${frame.body}")
                _errorFrames.tryEmit(frame)
            }

            else -> {
                logger.d(TAG, "Unhandled frame: ${frame.command}")
            }
        }
    }

    private fun negotiateHeartbeat(connectedFrame: StompFrame) {
        val serverHeartbeat = connectedFrame.headers["heart-beat"] ?: "0,0"
        val parts = serverHeartbeat.split(",")
        val serverSend = parts.getOrNull(0)?.toLongOrNull() ?: 0L
        val serverRecv = parts.getOrNull(1)?.toLongOrNull() ?: 0L

        clientHeartbeatMs = if (serverRecv > 0) maxOf(10000L, serverRecv) else 0L
        serverHeartbeatMs = if (serverSend > 0) maxOf(10000L, serverSend) else 0L

        logger.d(TAG, "Heart-beat negotiated: client→server=${clientHeartbeatMs}ms, server→client=${serverHeartbeatMs}ms")
        startHeartbeat()
    }

    private fun startHeartbeat() {
        heartbeatJob?.cancel()
        if (clientHeartbeatMs <= 0) return

        heartbeatJob = scope.launch {
            while (isActive && _connectionState.value == StompConnectionState.CONNECTED) {
                delay(clientHeartbeatMs)
                webSocket?.send("\n")
            }
        }
    }

    private fun resubscribeAll() {
        val subs = subscriptions.values.toList()
        if (subs.isEmpty()) {
            logger.w(TAG, "resubscribeAll() - no subscriptions to restore")
            return
        }
        subs.forEach { sub ->
            sendSubscribeFrame(sub)
            logger.i(TAG, "Re-subscribed: id=${sub.id} destination=${sub.destination}")
        }
    }

    private fun sendSubscribeFrame(subscription: StompSubscription) {
        sendFrame(
            StompFrame(
                StompCommand.SUBSCRIBE,
                mapOf(
                    "id" to subscription.id,
                    "destination" to subscription.destination
                )
            )
        )
    }

    private fun sendFrame(frame: StompFrame) {
        val encoded = frame.encode()
        val sent = webSocket?.send(encoded) ?: false
        if (!sent) {
            logger.w(TAG, "Failed to send frame: ${frame.command}")
        }
    }

    private fun scheduleReconnect() {
        if (!shouldReconnect) return
        if (reconnectJob?.isActive == true) return

        reconnectAttempts++
        val exp = (reconnectAttempts - 1).coerceIn(0, 5)
        val delayMs = (1000L shl exp).coerceAtMost(MAX_RECONNECT_DELAY_MS)

        reconnectJob = scope.launch {
            logger.d(TAG, "Reconnecting in ${delayMs}ms (attempt=$reconnectAttempts)")
            delay(delayMs)
            if (!shouldReconnect) return@launch
            connectInternal()
        }
    }
}
