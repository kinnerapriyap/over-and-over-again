package com.kinnerapriyap.overandoveragain.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.kinnerapriyap.overandoveragain.alarm.AlarmItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ListContent(
    groupedAlarms: List<List<AlarmItem>>,
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
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Clock(currentTime = currentTime)
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            if (groupedAlarms.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_alarms_yet),
                    modifier = Modifier.padding(32.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            } else {
                LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
                    items(groupedAlarms) { group ->
                        val times = group.map { it.time.convertToDisplayTime(locale = getLocale()) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                Text(text = times.joinToString())
                            }
                        }
                    }
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
