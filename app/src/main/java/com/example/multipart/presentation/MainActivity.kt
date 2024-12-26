package com.example.multipart.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multipart.data.DownloadState
import com.example.multipart.presentation.theme.MultipartTheme
import java.io.File
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultipartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
private fun App(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = viewModel<MainViewModel>()
    val uiState = viewModel.uiState

    val pickImageLauncher =
        rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            val fileName = "${UUID.randomUUID()}.jpg"
            uri?.let { uri ->
                val file = File(context.cacheDir, fileName)
                file.writeBytes(context.contentResolver.openInputStream(uri)?.readBytes()!!)
                viewModel.uploadImage(file)
            }
        }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                pickImageLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            },
        ) {
            Text(text = "Upload Image")
        }

        when {
            uiState.success -> {
                Text(text = "Success")
            }

            uiState.error -> {
                Text(text = "Error")
            }

            else -> {
                CircularProgressIndicator(progress = { uiState.progress.toFloat() })
            }
        }

        Button(
            onClick = {
                val fileName = "${UUID.randomUUID()}.jpg"
                val file = File(context.cacheDir, fileName)
                viewModel.downloadImage(file)
            },
        ) {
            Text(text = "Download Image")
        }

        when (uiState.downloadState) {
            is DownloadState.Downloading -> {
                CircularProgressIndicator(progress = { uiState.downloadState.progress.toFloat() })
            }

            DownloadState.Failed -> {
                Text(text = "Error")
            }

            DownloadState.Finished -> {
                Text(text = "Success")
            }
        }
    }
}
