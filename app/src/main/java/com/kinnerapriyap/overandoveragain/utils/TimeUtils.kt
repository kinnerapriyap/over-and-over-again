package com.kinnerapriyap.overandoveragain.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val MINUTE_MILLIS = 60 * 1000L
const val HOUR_MILLIS = 60 * 60 * 1000L
const val DEFAULT_DELAY = 5 * MINUTE_MILLIS

enum class IntervalType(val millis: Long) {
    Minutes(MINUTE_MILLIS),
    Hours(HOUR_MILLIS)
}

fun Long.convertToDisplayTime(pattern: String = "HH:mm", locale: Locale): String =
    SimpleDateFormat(pattern, locale).format(Date(this))

fun Calendar.convertToDisplayTime(pattern: String = "HH:mm", locale: Locale): String =
    SimpleDateFormat(pattern, locale).format(Date(timeInMillis))

fun Long.toTimeNumber(intervalType: IntervalType): Long = when (intervalType) {
    IntervalType.Minutes -> this / MINUTE_MILLIS
    IntervalType.Hours -> this / HOUR_MILLIS
}

fun Long.toTimeText(): String {
    val hours = this / 3600000
    val minutes = this % 3600000 / 60000
    val seconds = this % 60000 / 1000
    return when {
        hours == 0L && minutes == 0L -> "$seconds seconds"
        hours == 0L && seconds == 0L -> "$minutes minutes"
        hours == 0L -> "$minutes minutes $seconds seconds"
        minutes == 0L && seconds == 0L -> "$hours hours"
        minutes == 0L -> "$hours hours $seconds seconds"
        seconds == 0L -> "$hours hours $minutes minutes"
        else -> "$hours hours $minutes minutes $seconds seconds"
    }
}

@Composable
@ReadOnlyComposable
fun getLocale(): Locale {
    val configuration = LocalConfiguration.current
    return ConfigurationCompat.getLocales(configuration).get(0)
        ?: LocaleListCompat.getDefault()[0]!!
}
