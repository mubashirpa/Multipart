package com.example.multipart.data

interface ProgressCallback {
    fun onProgress(progress: Long)

    fun onSuccess(file: String)

    fun onError(error: Throwable)
}
