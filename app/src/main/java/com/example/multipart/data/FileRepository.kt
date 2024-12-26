package com.example.multipart.data

import com.example.multipart.core.utils.saveFileAsFlow
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class FileRepository {
    private val api = FileApi.Companion.instance

    fun uploadImage(
        file: File,
        callback: ProgressCallback,
    ): Boolean =
        try {
            val progressBody = ProgressRequestBody(file, callback)
            val part = MultipartBody.Part.createFormData("image", file.name, progressBody)

            api
                .uploadImage(part)
                .enqueue(RetroProgressCallback(callback))
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: HttpException) {
            e.printStackTrace()
            false
        }

    suspend fun downloadImage(file: File): Flow<DownloadState> = api.downloadImage().saveFileAsFlow(file)
}
