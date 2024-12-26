package com.example.multipart.core.utils

import com.example.multipart.data.DownloadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File

private const val DEFAULT_BUFFER_SIZE = 2048

fun ResponseBody.saveFile(file: File) {
    byteStream().use { inputStream ->
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
}

fun ResponseBody.saveFileAsFlow(file: File): Flow<DownloadState> =
    flow {
        emit(DownloadState.Downloading(0))

        try {
            byteStream().use { inputStream ->
                file.outputStream().use { outputStream ->
                    val totalBytes = contentLength()
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var progressBytes = 0L
                    var bytes = inputStream.read(buffer)
                    while (bytes >= 0) {
                        outputStream.write(buffer, 0, bytes)
                        progressBytes += bytes
                        bytes = inputStream.read(buffer)
                        emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                    }
                }
            }
            emit(DownloadState.Finished)
        } catch (_: Exception) {
            emit(DownloadState.Failed)
        }
    }.flowOn(Dispatchers.IO)
        .distinctUntilChanged()
