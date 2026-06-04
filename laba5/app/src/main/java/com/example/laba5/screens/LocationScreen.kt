package com.example.laba5.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlin.math.*

@Composable
fun LocationScreen() {

    val context = LocalContext.current

    val fusedLocationClient =
        remember {
            LocationServices.getFusedLocationProviderClient(context)
        }

    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var accuracy by remember { mutableStateOf<Float?>(null) }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    var lastUpdate by remember {
        mutableStateOf("")
    }

    val targetLat = 48.2915
    val targetLon = 25.9403

    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {

        val r = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a =
            sin(dLat / 2).pow(2) +
                    cos(Math.toRadians(lat1)) *
                    cos(Math.toRadians(lat2)) *
                    sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c
    }

    @SuppressLint("MissingPermission")
    fun loadLocation() {

        loading = true
        error = null

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                loading = false

                if (location != null) {

                    latitude = location.latitude
                    longitude = location.longitude
                    accuracy = location.accuracy

                    lastUpdate =
                        java.text.SimpleDateFormat(
                            "HH:mm:ss"
                        ).format(java.util.Date())

                } else {
                    error = "Локацію не знайдено"
                }
            }
            .addOnFailureListener {

                loading = false
                error = "Помилка отримання локації"
            }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                loadLocation()
            } else {
                error = "Дозвіл відхилено"
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)

    ) {

        if (loading) {
            CircularProgressIndicator()
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
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

        latitude?.let {
            Text("Latitude: $it")
        }

        longitude?.let {
            Text("Longitude: $it")
        }

        accuracy?.let {
            Text("Accuracy: ${it.toInt()} м")
        }

        if (lastUpdate.isNotEmpty()) {
            Text("Оновлено: $lastUpdate")
        }

        if (latitude != null && longitude != null) {
            val distance =
                calculateDistance(
                    latitude!!,
                    longitude!!,
                    targetLat,
                    targetLon
                )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Відстань до точки: %.2f км"
                    .format(distance)
            )
        }


        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        loadLocation()
                    }

                    else -> {
                        permissionLauncher.launch(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📍 Оновити локацію")
        }
    }
}