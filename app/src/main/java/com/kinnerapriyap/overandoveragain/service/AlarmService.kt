package com.kinnerapriyap.overandoveragain.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.app.NotificationCompat
import com.kinnerapriyap.overandoveragain.CHANNEL_ID
import com.kinnerapriyap.overandoveragain.R
import com.kinnerapriyap.overandoveragain.alarm.AlarmActivity
import com.kinnerapriyap.overandoveragain.alarm.EXTRA_MESSAGE
import com.kinnerapriyap.overandoveragain.alarm.REQUEST_CODE
import org.koin.android.ext.android.inject

internal const val ACTION_ALARM = "ALARM"

class AlarmService : Service() {
    private val viewModel: ServiceViewModel by inject()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == ACTION_ALARM) {
            val message = intent.getStringExtra(EXTRA_MESSAGE)
            val requestCode = intent.getIntExtra(REQUEST_CODE, 0)
            Toast.makeText(this, "Received alarm: $message $requestCode", LENGTH_SHORT).show()
            viewModel.delete(requestCode)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notification = createNotification(message)
            notificationManager.notify(requestCode, notification)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(requestCode, notification, FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED)
            } else {
                startForeground(requestCode, notification)
            }
        } else {
            Toast.makeText(this, "No action", LENGTH_SHORT).show()
        }
        return START_STICKY
    }

    private fun createNotification(message: String?): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
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
            .apply { flags = flags or Notification.FLAG_INSISTENT }

    override fun onBind(intent: Intent?): IBinder? = null
}
