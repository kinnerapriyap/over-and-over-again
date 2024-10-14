package com.kinnerapriyap.overandoveragain

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Calendar

data class AlarmItem(
    val alarmTime: Calendar,
    val delaySeconds: Long,
    val noOfAlarms: Int,
    val message: String
)

interface AlarmScheduler {
    fun schedule(alarmItem: AlarmItem)
    fun cancel(alarmItem: AlarmItem)
}

class DefaultAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(alarmItem: AlarmItem) {
        if (alarmManager.canScheduleExactAlarms()) {
            Log.e("DefaultAlarmScheduler", "Alarms are being scheduled")
            repeat(alarmItem.noOfAlarms) { count ->
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("EXTRA_MESSAGE", alarmItem.message + alarmItem.delaySeconds * count)
                }
                val alarmTime =
                    alarmItem.alarmTime.apply {
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis + count * alarmItem.delaySeconds * 1000
                alarmManager.setExactAndAllowWhileIdle(
                    RTC_WAKEUP,
                    alarmTime,
                    PendingIntent.getBroadcast(
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
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}
