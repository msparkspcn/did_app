package com.example.didapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DidEntity::class], version = 1, exportSchema = false)
abstract class DidDatabase : RoomDatabase() {
    abstract fun didDao(): DidDao
}
