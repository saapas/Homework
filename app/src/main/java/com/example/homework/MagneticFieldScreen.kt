package com.example.homework

import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import android.Manifest

@Composable
fun MagneticFieldScreen(navController: NavController) {
    val context = LocalContext.current
    val magneticCompass = remember { MagneticCompass(context) }
    val notificationService = remember { CNotificationService(context) }

    // State to track whether notifications are enabled
    var isCompassEnabled by remember { mutableStateOf(false) }

    val areNotificationsEnabled = remember { notificationService.areNotificationsEnabled() }

    // State to track the previous north state
    var wasPreviouslyNorth by remember { mutableStateOf(false) }

    fun requestNotificationPermissions() {
        val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
        val permissionStatus = ContextCompat.checkSelfPermission(context, notificationPermission)

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(notificationPermission),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Start or stop the compass based on the button state
    LaunchedEffect(isCompassEnabled) {
        if (isCompassEnabled) {
            magneticCompass.start() // Start the compass when notifications are enabled
        } else {
            magneticCompass.stop() // Stop the compass when notifications are disabled
        }
    }

    // Check if the device is pointing north and trigger a notification
    LaunchedEffect(magneticCompass.azimuth.value, isCompassEnabled) {
        if (isCompassEnabled && areNotificationsEnabled) { // Only check if notifications are enabled
            val azimuth = magneticCompass.azimuth.value
            val isNorth = azimuth in -10f..10f // Check if azimuth is within ±10 degrees of north

            // Only send a notification if the state has changed
            if (isNorth != wasPreviouslyNorth) {
                notificationService.showNotification(isNorth) // Notify when the state changes
                wasPreviouslyNorth = isNorth // Update the previous state
            }
        }
    }

    // Display the magnetic field values and azimuth
    // UI Layout
    Column(modifier = Modifier.padding(16.dp)) {
        // Button to enable/disable notifications
        Button(onClick = {
            if (!areNotificationsEnabled) {
                // Request notification permissions if not already granted
                requestNotificationPermissions()
            } else {
                // Toggle notifications
                isCompassEnabled = !isCompassEnabled
            }
        }) {
            Text(
                text = when {
                    !areNotificationsEnabled -> "Enable Notifications (Permissions Required)"
                    isCompassEnabled -> "Disable Notifications"
                    else -> "Enable Notifications"
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Magnetic Field Sensor Values:")
        Text(text = "X: ${magneticCompass.sensorValues.value.getOrNull(0) ?: 0f}")
        Text(text = "Y: ${magneticCompass.sensorValues.value.getOrNull(1) ?: 0f}")
        Text(text = "Z: ${magneticCompass.sensorValues.value.getOrNull(2) ?: 0f}")
        Text(text = "Azimuth: ${magneticCompass.azimuth.value} degrees")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("Screen2") {
                popUpTo("Screen2") { inclusive = true }
            }
        }) {
            Text(text = "Go To Screen2")
        }
    }
}

private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
