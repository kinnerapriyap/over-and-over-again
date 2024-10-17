package com.kinnerapriyap.overandoveragain.alarm

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    val alarms: Flow<List<AlarmItem>>

    @WorkerThread
    suspend fun insert(alarm: AlarmItem)

    @WorkerThread
    suspend fun insertAll(alarms: List<AlarmItem>)
}

class DefaultAlarmRepository(private val alarmDao: AlarmDao) : AlarmRepository {

    override val alarms: Flow<List<AlarmItem>> = alarmDao.getAll()

    @WorkerThread
    override suspend fun insert(alarm: AlarmItem) {
        alarmDao.insert(alarm)
    }

    @WorkerThread
    override suspend fun insertAll(alarms: List<AlarmItem>) {
        alarmDao.insertAll(alarms)
    }
}
