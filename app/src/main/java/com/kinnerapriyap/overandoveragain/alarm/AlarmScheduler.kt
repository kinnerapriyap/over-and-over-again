package com.kinnerapriyap.overandoveragain.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import java.util.Calendar

interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}

const val REQUEST_CODE = "REQUEST_CODE"
const val EXTRA_MESSAGE = "EXTRA_MESSAGE"

class DefaultAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(alarmItem: AlarmItem) {
        if (alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Alarms are being scheduled", LENGTH_SHORT).show()
            repeat(alarmItem.count) { count ->
                val intent = Intent(context, AlarmService::class.java).apply {
                    action = "ALARM"
                    putExtra(EXTRA_MESSAGE, alarmItem.message + alarmItem.delay * count)
                    putExtra(REQUEST_CODE, alarmItem.hashCode())
                }
                val alarmTime =
                    alarmItem.time.apply {
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis + count * alarmItem.delay
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(alarmTime, null),
                    PendingIntent.getService(
                        context,
                        alarmItem.hashCode() + count,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }
        }
    }

    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getService(
                context,
                alarmItem.hashCode(),
                Intent(context, AlarmService::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}
