package com.example.didapp.data.remote

import com.example.didapp.data.local.DidEntity
import retrofit2.http.GET

data class DidResponse(
    val items: List<DidItemDto>
)

data class DidItemDto(
    val id: String,
    val type: String,
    val content: String
)

interface DidApi {
    @GET("did-content")
    suspend fun getDidContent(): DidResponse
}
