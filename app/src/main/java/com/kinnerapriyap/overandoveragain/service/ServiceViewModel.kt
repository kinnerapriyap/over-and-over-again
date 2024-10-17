package com.kinnerapriyap.overandoveragain.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinnerapriyap.overandoveragain.alarm.AlarmRepository
import kotlinx.coroutines.launch

interface ServiceViewModel {
    fun delete(alarmId: Int)
}

class DefaultServiceViewModel(
    private val repository: AlarmRepository
) : ServiceViewModel, ViewModel() {
    override fun delete(alarmId: Int) {
        viewModelScope.launch {
            repository.delete(alarmId)
        }
    }
}
