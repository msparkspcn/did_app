package com.secta9ine.didapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.secta9ine.didapp.v2.data.local.V2AssetEntity
import com.secta9ine.didapp.v2.data.local.V2SnapshotDao
import com.secta9ine.didapp.v2.data.local.V2SnapshotEntity
import com.secta9ine.didapp.v2.data.local.V2ZoneEntity
import com.secta9ine.didapp.v2.data.local.V2ZonePlaylistItemEntity

@Database(
    entities = [
        DidEntity::class,
        V2SnapshotEntity::class,
        V2ZoneEntity::class,
        V2AssetEntity::class,
        V2ZonePlaylistItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class DidDatabase : RoomDatabase() {
    abstract fun didDao(): DidDao
    abstract fun v2SnapshotDao(): V2SnapshotDao
}
