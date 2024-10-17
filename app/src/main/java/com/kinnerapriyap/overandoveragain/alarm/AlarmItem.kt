package com.kinnerapriyap.overandoveragain.alarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class AlarmItem(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "time") val time: Calendar,
    @ColumnInfo(name = "delay") val delay: Long,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "message") val message: String
)
