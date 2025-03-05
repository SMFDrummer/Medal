package smf.talkweb.medal.ui.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File
import java.io.FileOutputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun File.lastModifiedDate(): Pair<String, String> = try {
    val instant = Instant.fromEpochMilliseconds(lastModified())
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    with(localDateTime) {
        "${year}/${monthNumber.toString().padStart(2, '0')}/${dayOfMonth.toString().padStart(2, '0')}"
    } to with(localDateTime) {
        "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}"
    }
} catch (e: Exception) {
    Log.d("lastModifiedDate", e.stackTraceToString())
    "" to ""
}

suspend fun Context.copyToPrivate(from: Uri, to: String, suffix: String = ""): Boolean =
    withContext(Dispatchers.IO) {
        try {
            val usersDir = getExternalFilesDir(to)?.apply {
                if (!exists()) mkdirs()
            } ?: error("getExternalFilesDir returns null")

            val fileName = DocumentFile.fromSingleUri(this@copyToPrivate, from)?.name?.substringBeforeLast('.')
                ?: contentResolver.query(from, arrayOf(OpenableColumns.DISPLAY_NAME),
                    null, null, null)?.use { cursor ->
                    cursor.moveToFirst()
                    cursor.getString(0).substringBeforeLast('.')
                } ?: error("Cannot get file name")

            val fileNameWithExtension = fileName + suffix
            val destFile = File(usersDir, fileNameWithExtension)

            contentResolver.openInputStream(from)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                    output.fd.sync()
                    true
                }
            } ?: false
        } catch (e: Exception) {
            Log.d("copyToPrivate", e.stackTraceToString())
            false
        }
    }

suspend fun Context.generateToPrivate(from: Any?, to: String, byName: String): Boolean =
    withContext(Dispatchers.IO) {
        try {
            val usersDir = getExternalFilesDir(to)?.apply {
                if (!exists()) mkdirs()
            } ?: error("getExternalFilesDir returns null")

            val sanitizedFileName = byName.replace("/", "_").replace("\\", "_")
            val targetFile = File(usersDir, sanitizedFileName)

            when (from) {
                is ByteArray -> {
                    targetFile.outputStream().buffered().use { outputStream ->
                        outputStream.write(from)
                    }
                }
                else -> {
                    val content = when (from) {
                        null -> "null"
                        is String -> from
                        is Number -> from.toString()
                        is Boolean -> from.toString()
                        else -> error("不支持的类型: ${from::class}")
                    }
                    targetFile.bufferedWriter().use { writer ->
                        writer.write(content)
                        writer.flush()
                    }
                }
            }
            true
        } catch (e: Exception) {
            Log.d("generateToPrivate", e.stackTraceToString())
            false
        }
    }

suspend fun Context.fetchFromPrivate(from: String, suffix: String = ""): List<File> =
    withContext(Dispatchers.IO) {
        try {
            val usersDir = getExternalFilesDir(from) ?: error("getExternalFilesDir returns null")

            usersDir.listFiles()?.let { files ->
                files.filter { file ->
                    file.isFile && file.canRead() &&
                            (suffix.isBlank() || file.name.contains(".", ignoreCase = true).let {
                                val targetSuffix = suffix.removePrefix(".").lowercase()
                                val fileSuffix = file.name.substringAfterLast('.', "").lowercase()
                                fileSuffix == targetSuffix
                            })
                }.sortedByDescending {
                    it.lastModified()
                }
            } ?: emptyList()
        } catch (e: Exception) {
            Log.d("fetchFromPrivate", e.stackTraceToString())
            emptyList()
        }
    }

suspend fun Context.deleteFromPrivate(locate: String, fileName: String): Boolean =
    withContext(Dispatchers.IO) {
        try {
            val usersDir = getExternalFilesDir(locate) ?: error("getExternalFilesDir returns null")
            val targetFile = File(usersDir, fileName).also {
                if (!it.absolutePath.startsWith(usersDir.absolutePath)) {
                    Log.e("deleteFromPrivate", "非法文件路径: ${it.absolutePath}")
                    throw SecurityException("禁止访问外部文件")
                }
            }
            when {
                !targetFile.exists() -> {
                    Log.d("deleteFromPrivate", "文件不存在: $fileName")
                    false
                }
                targetFile.isDirectory -> {
                    Log.w("deleteFromPrivate", "尝试删除目录: $fileName")
                    false
                }
                targetFile.delete() -> {
                    Log.i("deleteFromPrivate", "删除成功: $fileName")
                    true
                }
                else -> {
                    Log.e("deleteFromPrivate", "删除失败: $fileName")
                    false
                }
            }
        } catch (e: Exception) {
            Log.d("fetchFromPrivate", e.stackTraceToString())
            false
        }
    }
