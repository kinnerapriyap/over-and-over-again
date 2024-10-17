package com.kinnerapriyap.overandoveragain.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

interface AlarmViewModel {
    val repeatingAlarms: Flow<List<AlarmItem>>
    fun scheduleRepeatingAlarm(repeatingAlarmRequest: RepeatingAlarmRequest)
}

class DefaultAlarmViewModel(
    private val repository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
) : AlarmViewModel, ViewModel() {

    override val repeatingAlarms: Flow<List<AlarmItem>> = repository.alarms

    override fun scheduleRepeatingAlarm(repeatingAlarmRequest: RepeatingAlarmRequest) {
        viewModelScope.launch {
            val repeatingAlarmId = UUID.randomUUID().toString()
            val alarms = repeatingAlarmRequest.run {
                List(repeatingAlarmRequest.count) { count ->
                    val requestCode = hashCode() + count
                    val message = message + delay * count
                    val time = time + count * delay
                    AlarmItem(
                        id = requestCode,
                        repeatingAlarmId = repeatingAlarmId,
                        time = time,
                        message = message
                    )
                }
            }
            repository.insertAll(alarms)
            alarmScheduler.scheduleRepeatingAlarm(alarms)
        }
    }
}
