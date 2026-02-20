package com.example.didapp.data.repository

import android.util.Log
import com.example.didapp.data.local.DidDao
import com.example.didapp.data.local.DidEntity
import com.example.didapp.data.remote.DidApi
import com.example.didapp.util.AssetDownloader
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DidRepository @Inject constructor(
    private val api: DidApi,
    private val dao: DidDao,
    private val downloader: AssetDownloader
) {
    private val sampleItems = listOf(
        DidEntity(id = "sample_1", type = "IMAGE", content = "https://images.unsplash.com/photo-1542281286-9e0a16bb7366"),
        DidEntity(id = "sample_2", type = "VIDEO", content = "https://www.w3schools.com/html/mov_bbb.mp4"),
        DidEntity(id = "sample_3", type = "IMAGE", content = "https://images.unsplash.com/photo-1502134249126-9f3755a50d78"),
        DidEntity(id = "sample_4", type = "TEXT", content = "Welcome to our Premium Store! Enjoy 20% off on all new collections. Visit us today for an exclusive experience.")
    )

    val allDidItems: Flow<List<DidEntity>> = dao.getAllDidItems()

    suspend fun syncWithRemote() {
        try {
            val response = api.getDidContent()
            val entities = response.items.map { dto ->
                DidEntity(
                    id = dto.id,
                    type = dto.type,
                    content = dto.content
                )
            }
            dao.insertItems(entities)
            
            // Download assets for IMAGE and VIDEO
            entities.forEach { entity ->
                if (entity.type == "IMAGE" || entity.type == "VIDEO") {
                    val fileName = "${entity.id}_${entity.content.substringAfterLast("/")}"
                    val localPath = downloader.downloadFile(entity.content, fileName)
                    if (localPath != null) {
                        dao.updateLocalPath(entity.id, localPath)
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("DidRepository", "Sync failed (likely due to missing backend). Falling back to sample data. Error: ${e.message}")
            // In case of error, if DB is empty, use sample data
            checkAndInjectSamples()
        }
    }

    private suspend fun checkAndInjectSamples() {
        val currentItems = dao.getAllDidItemsSync()
        if (currentItems.isEmpty()) {
            dao.insertItems(sampleItems)
            
            // Trigger download for samples
            sampleItems.forEach { entity ->
                if (entity.type == "IMAGE" || entity.type == "VIDEO") {
                    val fileName = "${entity.id}_${entity.content.substringAfterLast("/")}"
                    val localPath = downloader.downloadFile(entity.content, fileName)
                    if (localPath != null) {
                        dao.updateLocalPath(entity.id, localPath)
                    }
                }
            }
        }
    }
}
