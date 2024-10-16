package com.kinnerapriyap.overandoveragain

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        Log.e("AlarmReceiver", "Received alarm: $message")
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        context?.run {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(soundUri)
                .build()
            notification.flags = notification.flags or Notification.FLAG_INSISTENT
            notificationManager.notify(1, notification)
        }
    }
}
