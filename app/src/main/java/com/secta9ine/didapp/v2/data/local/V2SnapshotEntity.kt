package com.secta9ine.didapp.v2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "v2_snapshot")
data class V2SnapshotEntity(
    @PrimaryKey val id: Int = 1,
    val version: Long,
    val validFromEpochSec: Long? = null,
    val validToEpochSec: Long? = null,
    val layoutId: String,
    val canvasWidth: Int,
    val canvasHeight: Int,
    val coordinateSystem: String,
    val updatedAt: Long = System.currentTimeMillis()
)

