package com.secta9ine.didapp.v2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "v2_asset")
data class V2AssetEntity(
    @PrimaryKey val assetId: String,
    val type: String,
    val source: String,
    val localPath: String?,
    val metadataJson: String,
    val defaultDurationSec: Int?
)
