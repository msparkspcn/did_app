package com.secta9ine.didapp.data.repository

import android.util.Log
import com.secta9ine.didapp.data.local.DidDao
import com.secta9ine.didapp.data.local.DidEntity
import com.secta9ine.didapp.data.remote.DidApi
import com.secta9ine.didapp.util.AssetDownloader
import kotlinx.coroutines.flow.Flow
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DidRepository @Inject constructor(
    private val api: DidApi,
    private val dao: DidDao,
    private val downloader: AssetDownloader
) {
    private val sampleItems = listOf(
        DidEntity(id = "sample_1", type = "IMAGE", content = "https://images.unsplash.com/photo-1542281286-9e0a16bb7366?q=80&w=1920&h=1080&auto=format&fit=crop"),
        DidEntity(id = "sample_2", type = "VIDEO", content = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
        DidEntity(id = "sample_3", type = "IMAGE", content = "https://images.unsplash.com/photo-1502134249126-9f3755a50d78?q=80&w=1920&h=1080&auto=format&fit=crop"),
        DidEntity(id = "sample_4", type = "TEXT", content = "Welcome to Our Store! Experience the next generation of DID solutions.")
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
            replaceRemoteItems(entities)
            downloadAssets(entities)
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
            downloadAssets(sampleItems)
        }
    }

    private suspend fun replaceRemoteItems(entities: List<DidEntity>) {
        if (entities.isEmpty()) {
            dao.clearAll()
            return
        }
        val ids = entities.map { it.id }
        dao.deleteItemsNotIn(ids)
        dao.insertItems(entities)
    }

    private suspend fun downloadAssets(entities: List<DidEntity>) {
        entities.forEach { entity ->
            if (entity.type == "IMAGE" || entity.type == "VIDEO") {
                val localPath = downloader.downloadFile(entity.content, buildAssetFileName(entity))
                if (localPath != null) {
                    dao.updateLocalPath(entity.id, localPath)
                }
            }
        }
    }

    private fun buildAssetFileName(entity: DidEntity): String {
        val rawName = runCatching {
            val path = URI(entity.content).path.orEmpty()
            path.substringAfterLast('/').ifBlank { "asset" }
        }.getOrDefault("asset")
        val safeName = rawName.replace(Regex("[^A-Za-z0-9._-]"), "_")
        val hash = entity.content.hashCode().toUInt().toString(16)
        return "${entity.id}_${hash}_$safeName"
    }
}
