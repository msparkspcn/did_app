package com.secta9ine.didapp.v2.data.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
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
    private var webSocket: WebSocket? = null

    fun connect(
        wsUrl: String,
        didId: String,
        onSnapshot: (PlayerSnapshotDto) -> Unit
    ) {
        disconnect()
        val request = Request.Builder()
            .url("$wsUrl?didId=$didId")
            .build()
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                parseSnapshot(text)?.let(onSnapshot)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // Keep silent; initial API sync and cached DB keep rendering alive.
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "closing")
        webSocket = null
    }

    private fun parseSnapshot(text: String): PlayerSnapshotDto? {
        return runCatching {
            // Supports:
            // 1) direct snapshot JSON
            // 2) envelope { "type": "SNAPSHOT_UPDATED", "payload": {...snapshot...} }
            val jsonObj = gson.fromJson(text, JsonObject::class.java)
            if (jsonObj.has("payload")) {
                gson.fromJson(jsonObj.get("payload"), PlayerSnapshotDto::class.java)
            } else {
                gson.fromJson(text, PlayerSnapshotDto::class.java)
            }
        }.getOrNull()
    }
}

