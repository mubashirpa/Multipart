package com.example.multipart.presentation

import com.example.multipart.data.DownloadState

data class MainUiState(
    val progress: Long = 0,
    val success: Boolean = false,
    val error: Boolean = false,
    val downloadState: DownloadState = DownloadState.Downloading(0),
)
