package com.example.laba5.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun CameraScreen() {

    val context = LocalContext.current


    val file = remember { File(context.filesDir, "photo.jpg") }

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    var imageUri by remember {
        mutableStateOf<Uri?>(if (file.exists()) uri else null)
    }


    var permissionDenied by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = uri
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            permissionDenied = false
            cameraLauncher.launch(uri)
        } else {
            // Користувач відмовив
            permissionDenied = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        if (permissionDenied) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Для роботи камери потрібен дозвіл. Будь ласка, надайте його в налаштуваннях.",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:${context.packageName}")
                            )
                            context.startActivity(intent)
                        }
                    ) {
                        Text("Перейти в налаштування")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }


        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Зроблене фото",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }


        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        permissionDenied = false
                        cameraLauncher.launch(uri)
                    }
                    else -> {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📷 Відкрити камеру")
        }
    }
}