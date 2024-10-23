package com.kinnerapriyap.overandoveragain

import android.annotation.SuppressLint
import android.app.AlarmManager
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kinnerapriyap.overandoveragain.alarm.RepeatingAlarmRequest
import com.kinnerapriyap.overandoveragain.ui.composables.MainContent
import com.kinnerapriyap.overandoveragain.ui.theme.OverandoveragainTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val alarmViewModel by viewModel<DefaultMainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repeatingAlarms by alarmViewModel.repeatingAlarms.collectAsStateWithLifecycle(
                emptyList()
            )
            OverandoveragainTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {

                        }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(R.string.add_alarms)
                            )
                        }
                    }
                ) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        alarms = repeatingAlarms,
                        onClick = { onClickEvent(it) }
                    )
                }
            }
        }
    }

    @SuppressLint("BatteryLife")
    override fun onResume() {
        super.onResume()
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
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

    private fun onClickEvent(clickEvent: ClickEvent) = when (clickEvent) {
        is ClickEvent.ScheduleRepeatingAlarm -> {
            val repeatingAlarmRequest = RepeatingAlarmRequest(
                time = clickEvent.time,
                delay = clickEvent.delay,
                count = clickEvent.count,
                message = clickEvent.message
            )
            alarmViewModel.scheduleRepeatingAlarm(repeatingAlarmRequest)
        }
    }
}

internal const val CHANNEL_ID = "alarm_id"

sealed interface ClickEvent {
    data class ScheduleRepeatingAlarm(
        val time: Long,
        val delay: Long,
        val count: Int,
        val message: String
    ) : ClickEvent
}
