package com.secta9ine.didapp.v2.data.remote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
import com.secta9ine.didapp.v2.contract.PowerScheduleDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnapshotWebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {
    private val reconnectScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var webSocket: WebSocket? = null
    private var reconnectJob: Job? = null
    private var reconnectAttempts: Int = 0
    private var shouldReconnect: Boolean = false

    private var wsUrl: String? = null
    private var didId: String? = null
    private var jwtToken: String? = null
    private var onSnapshot: ((PlayerSnapshotDto) -> Unit)? = null
    private var onPowerSchedule: ((PowerScheduleDto) -> Unit)? = null

    fun connect(
        wsUrl: String,
        didId: String,
        jwtToken: String,
        onSnapshot: (PlayerSnapshotDto) -> Unit,
        onPowerSchedule: ((PowerScheduleDto) -> Unit)? = null
    ) {
        disconnect()
        shouldReconnect = true
        this.wsUrl = wsUrl
        this.didId = didId
        this.jwtToken = jwtToken
        this.onSnapshot = onSnapshot
        this.onPowerSchedule = onPowerSchedule
        reconnectAttempts = 0
        reconnectJob?.cancel()
        connectInternal()
    }

    private fun connectInternal() {
        val baseUrl = wsUrl ?: return
        val currentDidId = didId ?: return
        val token = jwtToken ?: return

        val request = Request.Builder()
            .url("$baseUrl?didId=$currentDidId&token=$token")
            .build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                reconnectAttempts = 0
                Log.d("SnapshotWS", "Connected didId=$currentDidId")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                parseMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                this@SnapshotWebSocketClient.webSocket = null
                Log.w("SnapshotWS", "Connection failure didId=$currentDidId: ${t.message}")
                scheduleReconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                this@SnapshotWebSocketClient.webSocket = null
                Log.w("SnapshotWS", "Closed didId=$currentDidId code=$code reason=$reason")
                scheduleReconnect()
            }
        })
    }

    fun disconnect() {
        shouldReconnect = false
        reconnectJob?.cancel()
        webSocket?.close(1000, "closing")
        webSocket = null
    }

    private fun scheduleReconnect() {
        if (!shouldReconnect) return
        if (reconnectJob?.isActive == true) return

        reconnectAttempts += 1
        val exp = (reconnectAttempts - 1).coerceIn(0, 5)
        val delayMs = (1000L shl exp).coerceAtMost(30_000L)

        reconnectJob = reconnectScope.launch {
            Log.d("SnapshotWS", "Reconnecting in ${delayMs}ms (attempt=$reconnectAttempts)")
            delay(delayMs)
            if (!shouldReconnect) return@launch
            connectInternal()
        }
    }

    private fun parseMessage(text: String) {
        runCatching {
            val jsonObj = gson.fromJson(text, JsonObject::class.java)
            val type = jsonObj.get("type")?.asString

            when (type) {
                "POWER_SCHEDULE_UPDATED" -> {
                    val schedule = gson.fromJson(jsonObj.get("payload"), PowerScheduleDto::class.java)
                    onPowerSchedule?.invoke(schedule)
                }
                "SNAPSHOT_UPDATED" -> {
                    val snapshot = gson.fromJson(jsonObj.get("payload"), PlayerSnapshotDto::class.java)
                    onSnapshot?.invoke(snapshot)
                }
                else -> {
                    // Try parsing as direct snapshot JSON (no envelope)
                    val snapshot = gson.fromJson(text, PlayerSnapshotDto::class.java)
                    onSnapshot?.invoke(snapshot)
                }
            }
        }.onFailure { e ->
            Log.w("SnapshotWS", "Failed to parse WS message: ${e.message}")
        }
    }
}
