package com.example.homework

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class CNotificationService (private val context: Context){

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun areNotificationsEnabled(): Boolean {
        return notificationManager.areNotificationsEnabled()
    }

    fun showNotification(north: Boolean) {
        // Check if notifications are enabled before showing
        if (!areNotificationsEnabled()) {
            return // Exit if notifications are not enabled
        }

        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_android_24)
            .setContentTitle("Compass")
            .setContentText("Is pointing North: $north")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1,notification)
    }

    companion object{
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}