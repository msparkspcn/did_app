package com.secta9ine.didapp.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetDownloader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun downloadFile(url: String, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val directory = File(context.filesDir, "media")
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val file = File(directory, fileName)
                if (file.exists()) return@withContext file.absolutePath

                val connection = URL(url).openConnection()
                connection.connect()
                
                connection.getInputStream().use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun deleteFile(path: String) {
        withContext(Dispatchers.IO) {
            runCatching { File(path).delete() }
        }
    }
}
