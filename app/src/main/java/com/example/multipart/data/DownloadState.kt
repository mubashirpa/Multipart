package com.example.multipart.data

sealed class DownloadState {
    data class Downloading(
        val progress: Int,
    ) : DownloadState()

    object Finished : DownloadState()

    object Failed : DownloadState()
}
