package com.secta9ine.didapp.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class ApiResponse<T>(
    val code: String,
    val message: String,
    val timestamp: String,
    val data: T?
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val id: Long,
    val username: String,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val accessTokenExpiresIn: Long,
    val refreshTokenExpiresIn: Long,
    val roleCode: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class DeviceResponse(
    val deviceId: String,
    val storeId: String?,
    val name: String?,
    val serialNumber: String?,
    val model: String?,
    val appVersion: String?,
    val active: Boolean,
    val contentId: String?,
    val playlistId: String?,
    val createdAt: String?,
    val updatedAt: String?
)

// === 디바이스 인증 프로세스 DTO ===
data class DeviceStatusResponse(
    val deviceId: String? = null,
    val authStatus: String,           // NOT_FOUND, PENDING_APPROVAL, ACTIVE
    val authenticationCode: String? = null,
    val expiresAt: String? = null,
    val id: String? = null,
    val storeId: String? = null,
    val deviceName: String? = null
)

data class DeviceRegisterRequest(
    val deviceId: String,
    val serialNumber: String,
    val model: String? = null,
    val appVersion: String? = null
)

data class DeviceRegisterResponse(
    val deviceId: String,
    val authStatus: String,
    val authenticationCode: String? = null,
    val expiresAt: String? = null,
    val id: Long? = null,
    val storeId: Long? = null,
    val deviceName: String? = null
)

interface DidApi {

    // === 인증 ===
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): ApiResponse<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(@Body request: Map<String, String>): ApiResponse<Any>

    // === 디바이스 ===
    @GET("devices")
    suspend fun getDevices(): ApiResponse<List<DeviceResponse>>

    @GET("devices/{deviceId}")
    suspend fun getDevice(@Path("deviceId") deviceId: String): ApiResponse<DeviceResponse>

    // === 시스템 ===
    @GET("system/ping")
    suspend fun ping(): ApiResponse<Any>

    // === 디바이스 인증 프로세스 ===
    @GET("device-auth/{deviceId}")
    suspend fun getDeviceAuth(@Path("deviceId") deviceId: String): ApiResponse<DeviceStatusResponse>

    @POST("device-auth/register")
    suspend fun registerDevice(@Body request: DeviceRegisterRequest): ApiResponse<DeviceRegisterResponse>
}
