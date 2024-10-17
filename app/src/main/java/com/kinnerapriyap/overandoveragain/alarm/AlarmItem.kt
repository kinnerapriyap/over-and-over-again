package com.kinnerapriyap.overandoveragain.alarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
data class AlarmItem(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "repeating_alarm_id") val repeatingAlarmId: String,
    val time: Long,
    val message: String,
)
