package com.secta9ine.didapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// LEGACY (v1): Single full-screen item model.
// Keep for rollback/reference. The v2 multi-zone layout model is defined under
// com.secta9ine.didapp.v2.contract.
@Entity(tableName = "did_items")
data class DidEntity(
    @PrimaryKey val id: String,
    val type: String, // "IMAGE", "VIDEO", "TEXT"
    val content: String, // URL or Text body
    val localPath: String? = null, // Path to downloaded file
    val timestamp: Long = System.currentTimeMillis()
)
