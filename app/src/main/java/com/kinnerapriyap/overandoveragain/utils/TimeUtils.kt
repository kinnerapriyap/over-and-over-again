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

fun Long.convertToDisplayTime(pattern: String = "HH:mm", locale: Locale): String =
    SimpleDateFormat(pattern, locale).format(Date(this))

fun Calendar.convertToDisplayTime(pattern: String = "HH:mm", locale: Locale): String =
    SimpleDateFormat(pattern, locale).format(Date(timeInMillis))

fun Long.toText(): String {
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
