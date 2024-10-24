package com.kinnerapriyap.overandoveragain.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import com.kinnerapriyap.overandoveragain.ClickEvent
import com.kinnerapriyap.overandoveragain.R
import com.kinnerapriyap.overandoveragain.alarm.AlarmItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ListContent(
    alarms: List<AlarmItem>,
    currentTime: Triple<Int, Int, Int>,
    modifier: Modifier = Modifier,
    onClick: (ClickEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onClick(ClickEvent.AddAlarms)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_alarms)
                )
            }
        }
    ) { innerPadding ->
        Column {
            Clock(currentTime = currentTime)
            LazyColumn(modifier = modifier.padding(innerPadding)) {
                items(alarms.groupBy { it.repeatingAlarmId }.toList()) { (_, group) ->
                    val times = group.map { it.time.convertToDisplayTime(locale = getLocale()) }
                    Text(text = times.joinToString())
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

fun Long.convertToDisplayTime(pattern: String = "HH:mm:ss", locale: Locale): String =
    SimpleDateFormat(pattern, locale).format(Date(this))

@Composable
@ReadOnlyComposable
fun getLocale(): Locale {
    val configuration = LocalConfiguration.current
    return ConfigurationCompat.getLocales(configuration).get(0)
        ?: LocaleListCompat.getDefault()[0]!!
}
