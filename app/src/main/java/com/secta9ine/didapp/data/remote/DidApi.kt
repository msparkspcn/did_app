package com.secta9ine.didapp.data.remote

import com.secta9ine.didapp.data.local.DidEntity
import retrofit2.http.GET

// LEGACY (v1): Flat content list API.
// Keep for rollback/reference. The v2 snapshot contract is defined under
// com.secta9ine.didapp.v2.contract.
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
