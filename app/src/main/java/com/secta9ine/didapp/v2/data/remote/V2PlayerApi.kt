package com.secta9ine.didapp.v2.data.remote

import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
import com.secta9ine.didapp.v2.contract.PowerScheduleDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body

data class DeviceStatusDto(
    val didId: String,
    val status: String,
    val activatedAtEpochSec: Long? = null,
    val updatedAtEpochSec: Long? = null
)

data class DeviceRegisterRequestDto(
    val didId: String,
    val appVersion: String = "dev",
    val platform: String = "ANDROID"
)

data class DeviceRegisterResponseDto(
    val ok: Boolean,
    val didId: String,
    val status: String
)

interface V2PlayerApi {
    @GET("players/{didId}/device-status")
    suspend fun getDeviceStatus(@Path("didId") didId: String): DeviceStatusDto

    @POST("devices/register")
    suspend fun registerDevice(@Body request: DeviceRegisterRequestDto): DeviceRegisterResponseDto

    @GET("players/{didId}/snapshot")
    suspend fun getPlayerSnapshot(@Path("didId") didId: String): PlayerSnapshotDto

    @GET("players/{didId}/power-schedule")
    suspend fun getPowerSchedule(@Path("didId") didId: String): PowerScheduleDto
}
