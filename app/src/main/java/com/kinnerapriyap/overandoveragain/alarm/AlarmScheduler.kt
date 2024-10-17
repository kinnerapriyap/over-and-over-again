package com.kinnerapriyap.overandoveragain.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.kinnerapriyap.overandoveragain.service.AlarmService

interface AlarmScheduler {
    fun scheduleRepeatingAlarm(alarms: List<AlarmItem>)
    fun cancelRepeatingAlarm(repeatingAlarmId: String)
}

const val REQUEST_CODE = "REQUEST_CODE"
const val EXTRA_MESSAGE = "EXTRA_MESSAGE"

class DefaultAlarmScheduler(private val context: Context) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun scheduleRepeatingAlarm(alarms: List<AlarmItem>) {
        if (alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Alarms are being scheduled", LENGTH_SHORT).show()
            alarms.forEach {
                val intent = Intent(context, AlarmService::class.java).apply {
                    action = "ALARM"
                    putExtra(EXTRA_MESSAGE, it.message)
                    putExtra(REQUEST_CODE, it.id)
                }
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(it.time, null),
                    PendingIntent.getService(
                        context,
                        it.id,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }
        }
    }

    override fun cancelRepeatingAlarm(repeatingAlarmId: String) = Unit

    /*override fun cancelRepeatingAlarm(repeatingAlarmRequest: RepeatingAlarmRequest) {
        alarmManager.cancel(
            PendingIntent.getService(
                context,
                repeatingAlarmRequest.hashCode(),
                Intent(context, AlarmService::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }*/
}
