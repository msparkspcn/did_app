package com.secta9ine.didapp.system

import android.content.Context
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileLogger @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val TAG = "FileLogger"
        private const val LOG_DIR = "logs"
        private const val MAX_FILE_SIZE = 5 * 1024 * 1024L // 5MB
        private const val MAX_LOG_FILES = 5
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    }

    private val logDir: File by lazy {
        File(context.filesDir, LOG_DIR).also { it.mkdirs() }
    }

    private val logFile: File
        get() {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            return File(logDir, "did-app-$today.log")
        }

    fun d(tag: String, message: String) {
        write("D", tag, message)
        Log.d(tag, message)
    }

    fun i(tag: String, message: String) {
        write("I", tag, message)
        Log.i(tag, message)
    }

    fun w(tag: String, message: String) {
        write("W", tag, message)
        Log.w(tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        val msg = if (throwable != null) "$message\n${throwable.stackTraceToString()}" else message
        write("E", tag, msg)
        Log.e(tag, message, throwable)
    }

    private fun write(level: String, tag: String, message: String) {
        try {
            val timestamp = dateFormat.format(Date())
            val line = "$timestamp [$level] $tag: $message\n"
            val file = logFile

            if (file.exists() && file.length() > MAX_FILE_SIZE) {
                rotateFiles()
            }

            file.appendText(line)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to write log: ${e.message}")
        }
    }

    private fun rotateFiles() {
        val files = logDir.listFiles { f -> f.name.endsWith(".log") }
            ?.sortedByDescending { it.lastModified() }
            ?: return

        // 오래된 파일 삭제
        if (files.size >= MAX_LOG_FILES) {
            files.drop(MAX_LOG_FILES - 1).forEach { it.delete() }
        }
    }
}
