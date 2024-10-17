package com.kinnerapriyap.overandoveragain.alarm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm_table")
    fun getAll(): Flow<List<AlarmItem>>

    @Query("SELECT * FROM alarm_table WHERE repeating_alarm_id = :repeatingAlarmId")
    fun getGroup(repeatingAlarmId: String): Flow<List<AlarmItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarmItem: AlarmItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alarmItems: List<AlarmItem>)

    @Query("DELETE FROM alarm_table WHERE repeating_alarm_id = :repeatingAlarmId")
    suspend fun deleteGroup(repeatingAlarmId: String)

    @Query("DELETE FROM alarm_table WHERE id = :alarmId")
    suspend fun delete(alarmId: Int)

    @Query("DELETE FROM alarm_table")
    suspend fun deleteAll()
}
