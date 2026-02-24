package com.secta9ine.didapp.v2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "v2_zone")
data class V2ZoneEntity(
    @PrimaryKey val zoneId: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val zIndex: Int,
    val backgroundHex: String?,
    val fitMode: String
)

