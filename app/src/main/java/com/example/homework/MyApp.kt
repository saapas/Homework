package com.example.homework

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class MyApp:  Application(){
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CNotificationService.COUNTER_CHANNEL_ID,
            "Alarm",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used for the notification"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
