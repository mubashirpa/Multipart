package com.example.multipart.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multipart.data.FileRepository
import com.example.multipart.data.ProgressCallback
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val repository: FileRepository = FileRepository(),
) : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    fun uploadImage(file: File) {
        viewModelScope.launch {
            repository.uploadImage(
                file,
                object : ProgressCallback {
                    override fun onProgress(progress: Long) {
                        uiState = uiState.copy(progress = progress)
                    }

                    override fun onSuccess(file: String) {
                        uiState = uiState.copy(success = true)
                    }

                    override fun onError(error: Throwable) {
                        uiState = uiState.copy(error = true)
                    }
                },
            )
        }
    }

    fun downloadImage(file: File) {
        viewModelScope.launch {
            repository.downloadImage(file).collect { state ->
                uiState = uiState.copy(downloadState = state)
            }
        }
    }
}
