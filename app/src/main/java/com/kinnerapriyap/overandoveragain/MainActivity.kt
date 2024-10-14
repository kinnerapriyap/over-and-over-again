package com.kinnerapriyap.overandoveragain

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.kinnerapriyap.overandoveragain.ui.composables.MainContent
import com.kinnerapriyap.overandoveragain.ui.theme.OverandoveragainTheme
import java.time.LocalDateTime


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
        val alarmScheduler: AlarmScheduler = DefaultAlarmScheduler(this)
        var alarmItem: AlarmItem? = null
        setContent {
            OverandoveragainTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding)) {
                        when (it) {
                            is ClickEvent.ScheduleAlarm -> alarmItem = AlarmItem(
                                LocalDateTime.now().plusSeconds(it.seconds.toLong()),
                                it.message
                            ).also { alarmItem -> alarmScheduler.schedule(alarmItem) }

                            ClickEvent.CancelAlarm -> alarmItem?.let(alarmScheduler::cancel)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.setData(Uri.fromParts("package", packageName, null))
            startActivity(intent)
        }
    }

    private fun createNotificationChannel() {
        val channelId = "alarm_id"
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

sealed interface ClickEvent {
    data class ScheduleAlarm(val seconds: String, val message: String) : ClickEvent
    data object CancelAlarm : ClickEvent
}
