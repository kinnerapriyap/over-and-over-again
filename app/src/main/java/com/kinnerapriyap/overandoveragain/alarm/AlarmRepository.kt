package com.kinnerapriyap.overandoveragain.alarm

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    val alarms: Flow<List<AlarmItem>>

    suspend fun insert(alarm: AlarmItem)

    suspend fun insertAll(alarms: List<AlarmItem>)

    suspend fun delete(repeatingAlarmId: String)
}

@WorkerThread
class DefaultAlarmRepository(private val alarmDao: AlarmDao) : AlarmRepository {

    override val alarms: Flow<List<AlarmItem>> = alarmDao.getAll()

    override suspend fun insert(alarm: AlarmItem) {
        alarmDao.insert(alarm)
    }

    override suspend fun insertAll(alarms: List<AlarmItem>) {
        alarmDao.insertAll(alarms)
    }

    override suspend fun delete(repeatingAlarmId: String) {
        alarmDao.deleteGroup(repeatingAlarmId)
    }
}
