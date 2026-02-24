package com.secta9ine.didapp.v2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "v2_zone_playlist_item")
data class V2ZonePlaylistItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val zoneId: String,
    val assetId: String,
    val playOrder: Int,
    val durationSec: Int?,
    val transition: String
)

