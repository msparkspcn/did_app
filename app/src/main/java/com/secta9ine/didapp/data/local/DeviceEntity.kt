package com.secta9ine.didapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_info")
data class DeviceEntity(
    @PrimaryKey
    val deviceId: String,
    val authStatus: String,              // ACTIVE, PENDING_APPROVAL
    val authenticationCode: String? = null,
    val storeId: Long? = null,
    val deviceName: String? = null,
    val activatedAt: String? = null,     // 최초 활성화 시각 (ISO 8601)
    val lastSyncedAt: Long = System.currentTimeMillis()  // 마지막 서버 동기화 시각
)
