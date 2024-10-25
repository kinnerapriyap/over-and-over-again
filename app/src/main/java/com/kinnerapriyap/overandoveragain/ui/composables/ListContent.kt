package com.kinnerapriyap.overandoveragain.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import com.kinnerapriyap.overandoveragain.ClickEvent
import com.kinnerapriyap.overandoveragain.R
import com.kinnerapriyap.overandoveragain.RepeatingAlarmDisplayModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListContent(
    repeatingAlarms: List<RepeatingAlarmDisplayModel>,
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
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
            )
        },
        bottomBar = {

        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Clock(modifier = Modifier.padding(32.dp), time = currentTime)
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            if (repeatingAlarms.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_alarms_yet),
                    modifier = Modifier.padding(32.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            } else {
                LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
                    items(repeatingAlarms) { group ->
                        val startTime = group.startTime.convertToDisplayTime(locale = getLocale())
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = group.message)
                                    Text(
                                        text = startTime,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    if (group.interval != null) {
                                        Text(
                                            text = stringResource(
                                                R.string.alarm_interval_count,
                                                group.count,
                                                group.interval.toText()
                                            )
                                        )
                                    }
                                }
                                Clock(
                                    time = Triple(
                                        group.startTime.get(Calendar.HOUR),
                                        group.startTime.get(Calendar.MINUTE),
                                        group.startTime.get(Calendar.SECOND)
                                    ),
                                    clockSize = 80.dp,
                                    showSeconds = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

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
