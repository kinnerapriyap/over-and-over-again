package com.kinnerapriyap.overandoveragain

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class OverAndOverAgainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
