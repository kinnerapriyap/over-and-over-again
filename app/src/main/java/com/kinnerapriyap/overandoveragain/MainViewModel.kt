package com.kinnerapriyap.overandoveragain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinnerapriyap.overandoveragain.alarm.AlarmItem
import com.kinnerapriyap.overandoveragain.alarm.AlarmRepository
import com.kinnerapriyap.overandoveragain.alarm.AlarmScheduler
import com.kinnerapriyap.overandoveragain.alarm.RepeatingAlarmRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

interface MainViewModel {
    val repeatingAlarms: Flow<List<AlarmItem>>
    fun scheduleRepeatingAlarm(repeatingAlarmRequest: RepeatingAlarmRequest)
}

class DefaultMainViewModel(
    private val repository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
) : MainViewModel, ViewModel() {

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
