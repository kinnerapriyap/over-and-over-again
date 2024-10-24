package com.kinnerapriyap.overandoveragain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinnerapriyap.overandoveragain.alarm.AlarmItem
import com.kinnerapriyap.overandoveragain.alarm.AlarmRepository
import com.kinnerapriyap.overandoveragain.alarm.AlarmScheduler
import com.kinnerapriyap.overandoveragain.alarm.RepeatingAlarmRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

interface MainViewModel {
    val currentTime: StateFlow<Triple<Int, Int, Int>>
    val repeatingAlarms: Flow<List<AlarmItem>>
    fun scheduleRepeatingAlarm(repeatingAlarmRequest: RepeatingAlarmRequest)
}

class DefaultMainViewModel(
    private val repository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
) : MainViewModel, ViewModel() {
    private val _currentTime = MutableStateFlow<Triple<Int, Int, Int>>(Triple(0, 0, 0))
    override val currentTime: StateFlow<Triple<Int, Int, Int>> = _currentTime

    init {
        viewModelScope.launch {
            while (isActive) {
                val calendar = Calendar.getInstance()
                _currentTime.value =
                    Triple(
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND)
                    )
                delay(1000)
            }
        }
    }

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
