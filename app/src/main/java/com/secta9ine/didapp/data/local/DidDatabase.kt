package com.secta9ine.didapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DidEntity::class, DeviceEntity::class],
    version = 5,
    exportSchema = false
)
abstract class DidDatabase : RoomDatabase() {
    abstract fun didDao(): DidDao
    abstract fun deviceDao(): DeviceDao
}
