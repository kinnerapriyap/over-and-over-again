package com.kinnerapriyap.overandoveragain

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.kinnerapriyap.overandoveragain.alarm.AlarmItem
import com.kinnerapriyap.overandoveragain.alarm.AlarmScheduler
import com.kinnerapriyap.overandoveragain.alarm.DefaultAlarmScheduler
import com.kinnerapriyap.overandoveragain.ui.composables.MainContent
import com.kinnerapriyap.overandoveragain.ui.theme.OverandoveragainTheme
import java.util.Calendar
import java.util.UUID


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val alarmScheduler: AlarmScheduler = DefaultAlarmScheduler(this)
        var alarmItem: AlarmItem? = null
        setContent {
            OverandoveragainTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding)) {
                        when (it) {
                            is ClickEvent.ScheduleAlarm -> {
                                alarmItem = AlarmItem(
                                    id = UUID.randomUUID().toString(),
                                    time = it.time,
                                    delay = it.delay,
                                    count = it.count,
                                    message = it.message
                                ).also { alarmItem -> alarmScheduler.schedule(alarmItem) }
                            }

                            ClickEvent.CancelAlarm -> alarmItem?.let(alarmScheduler::cancel)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("BatteryLife")
    override fun onResume() {
        super.onResume()
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        } else if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }
}

internal const val CHANNEL_ID = "alarm_id"

sealed interface ClickEvent {
    data class ScheduleAlarm(
        val time: Calendar,
        val delay: Long,
        val count: Int,
        val message: String
    ) : ClickEvent

    data object CancelAlarm : ClickEvent
}
