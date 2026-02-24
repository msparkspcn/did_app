package com.secta9ine.didapp.v2.data.remote

import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
import retrofit2.http.GET
import retrofit2.http.Path

interface V2PlayerApi {
    @GET("players/{didId}/snapshot")
    suspend fun getPlayerSnapshot(@Path("didId") didId: String): PlayerSnapshotDto
}

