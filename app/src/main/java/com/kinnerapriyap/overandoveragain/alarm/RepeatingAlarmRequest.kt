package com.kinnerapriyap.overandoveragain.alarm

data class RepeatingAlarmRequest(
    val time: Long,
    val delay: Long,
    val count: Int,
    val message: String
)
