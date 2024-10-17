package com.kinnerapriyap.overandoveragain.alarm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.RingtoneManager
import android.os.IBinder
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.app.NotificationCompat
import com.kinnerapriyap.overandoveragain.AlarmActivity
import com.kinnerapriyap.overandoveragain.CHANNEL_ID
import com.kinnerapriyap.overandoveragain.R

class AlarmService: Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == "ALARM") {
            val message = intent.getStringExtra(EXTRA_MESSAGE)
            val requestCode = intent.getIntExtra(REQUEST_CODE, 0)
            Toast.makeText(this, "Received alarm: $message $requestCode", LENGTH_SHORT).show()
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(soundUri)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .setAutoCancel(true)
                .setTimeoutAfter(1000 * 60)
                .setContentIntent(
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, AlarmActivity::class.java),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                )
                .setFullScreenIntent(
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, AlarmActivity::class.java),
                        PendingIntent.FLAG_IMMUTABLE
                    ),
                    true
                )
                .build()
            notification.flags = notification.flags or Notification.FLAG_INSISTENT
            notificationManager.notify(requestCode, notification)
            startForeground(requestCode, notification)
        } else {
            Toast.makeText(this, "No action", LENGTH_SHORT).show()
        }
        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? = null
}
