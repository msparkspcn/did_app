package com.secta9ine.didapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device_info WHERE deviceId = :deviceId LIMIT 1")
    suspend fun getDevice(deviceId: String): DeviceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDevice(device: DeviceEntity)

    @Query("DELETE FROM device_info")
    suspend fun clearAll()
}
