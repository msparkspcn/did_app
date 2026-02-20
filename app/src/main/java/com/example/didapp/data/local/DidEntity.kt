package com.example.didapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "did_items")
data class DidEntity(
    @PrimaryKey val id: String,
    val type: String, // "IMAGE", "VIDEO", "TEXT"
    val content: String, // URL or Text body
    val localPath: String? = null, // Path to downloaded file
    val timestamp: Long = System.currentTimeMillis()
)
