package com.secta9ine.didapp.system

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "TokenManager"
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_ANDROID_ID = "android_id"
        private const val KEY_APPROVAL_CODE = "approval_code"
    }

    private val prefs get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun getDeviceId(): String? = prefs.getString(KEY_DEVICE_ID, null)

    fun getAndroidId(): String? = prefs.getString(KEY_ANDROID_ID, null)

    fun getApprovalCode(): String? = prefs.getString(KEY_APPROVAL_CODE, null)

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
        Log.i(TAG, "Tokens saved")
    }

    fun saveDeviceId(deviceId: String) {
        prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()
        Log.i(TAG, "Device ID saved: $deviceId")
    }

    fun saveAndroidId(androidId: String) {
        prefs.edit().putString(KEY_ANDROID_ID, androidId).apply()
    }

    fun saveApprovalCode(code: String) {
        prefs.edit().putString(KEY_APPROVAL_CODE, code).apply()
        Log.i(TAG, "Approval code saved: $code")
    }

    fun isAuthenticated(): Boolean = getAccessToken() != null

    fun clear() {
        prefs.edit().clear().apply()
        Log.i(TAG, "All auth data cleared")
    }
}
