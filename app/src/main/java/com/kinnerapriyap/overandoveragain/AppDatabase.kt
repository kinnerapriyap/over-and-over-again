package com.kinnerapriyap.overandoveragain

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kinnerapriyap.overandoveragain.alarm.AlarmDao
import com.kinnerapriyap.overandoveragain.alarm.AlarmItem

@Database(entities = [AlarmItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}
