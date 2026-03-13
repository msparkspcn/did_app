package com.secta9ine.didapp.v2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class V2SnapshotDao {
    @Query("SELECT * FROM v2_snapshot LIMIT 1")
    abstract fun observeSnapshot(): Flow<V2SnapshotEntity?>

    @Query("SELECT * FROM v2_zone ORDER BY zIndex ASC")
    abstract fun observeZones(): Flow<List<V2ZoneEntity>>

    @Query("SELECT * FROM v2_asset")
    abstract fun observeAssets(): Flow<List<V2AssetEntity>>

    @Query("SELECT * FROM v2_asset")
    abstract suspend fun getAssets(): List<V2AssetEntity>

    @Query("SELECT * FROM v2_zone_playlist_item ORDER BY zoneId ASC, playOrder ASC")
    abstract fun observeZonePlaylistItems(): Flow<List<V2ZonePlaylistItemEntity>>

    @Query("SELECT COUNT(*) > 0 FROM v2_snapshot")
    abstract suspend fun hasSnapshot(): Boolean

    @Query("SELECT version FROM v2_snapshot WHERE id = 1")
    abstract suspend fun getSnapshotVersion(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertSnapshot(entity: V2SnapshotEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertZones(entities: List<V2ZoneEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertAssets(entities: List<V2AssetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertZonePlaylistItems(entities: List<V2ZonePlaylistItemEntity>)

    @Query("DELETE FROM v2_zone")
    abstract suspend fun clearZones()

    @Query("DELETE FROM v2_asset")
    abstract suspend fun clearAssets()

    @Query("DELETE FROM v2_zone_playlist_item")
    abstract suspend fun clearZonePlaylistItems()

    @Transaction
    open suspend fun replaceSnapshot(
        snapshot: V2SnapshotEntity,
        zones: List<V2ZoneEntity>,
        assets: List<V2AssetEntity>,
        zonePlaylistItems: List<V2ZonePlaylistItemEntity>
    ) {
        upsertSnapshot(snapshot)
        clearZones()
        clearAssets()
        clearZonePlaylistItems()
        if (zones.isNotEmpty()) upsertZones(zones)
        if (assets.isNotEmpty()) upsertAssets(assets)
        if (zonePlaylistItems.isNotEmpty()) insertZonePlaylistItems(zonePlaylistItems)
    }
}
