package com.example.bundlemaker2.utils

import android.content.Context
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object LogUtils {
    private const val LOG_DIR = "logs"
    private const val LOG_FILE = "sync_log.txt"
    private const val MAX_LOG_SIZE = 1024 * 1024 // 1MB
    private const val MAX_LOG_FILES = 5

    private fun getLogFile(context: Context): File {
        val logDir = File(context.filesDir, LOG_DIR).apply {
            if (!exists()) mkdirs()
        }
        return File(logDir, LOG_FILE)
    }

    private fun rotateLogsIfNeeded(context: Context) {
        val logFile = getLogFile(context)
        if (logFile.length() > MAX_LOG_SIZE) {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
            val backupFile = File(logFile.parent, "sync_log_$timestamp.txt")
            logFile.renameTo(backupFile)

            // 古いログファイルを削除
            val logDir = File(context.filesDir, LOG_DIR)
            logDir.listFiles()?.let { files ->
                if (files.size > MAX_LOG_FILES) {
                    files.sortedBy { it.lastModified() }
                        .take(files.size - MAX_LOG_FILES)
                        .forEach { it.delete() }
                }
            }
        }
    }

    fun logSyncEvent(context: Context, message: String) {
        try {
            rotateLogsIfNeeded(context)

            val timestamp = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN).format(Date())
            val logMessage = "[$timestamp] $message\n"

            FileOutputStream(getLogFile(context), true).use { fos ->
                OutputStreamWriter(fos, Charsets.UTF_8).use { writer ->
                    writer.append(logMessage)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readLogs(context: Context): String {
        return try {
            getLogFile(context).readText(Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            "ログの読み込みに失敗しました"
        }
    }

    fun clearLogs(context: Context) {
        try {
            getLogFile(context).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}