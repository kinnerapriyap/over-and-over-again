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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kinnerapriyap.overandoveragain.ClickEvent
import com.kinnerapriyap.overandoveragain.R
import com.kinnerapriyap.overandoveragain.RepeatingAlarmDisplayModel
import com.kinnerapriyap.overandoveragain.utils.convertToDisplayTime
import com.kinnerapriyap.overandoveragain.utils.getLocale
import com.kinnerapriyap.overandoveragain.utils.toTimeText
import kotlinx.collections.immutable.ImmutableList
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListContent(
    repeatingAlarms: ImmutableList<RepeatingAlarmDisplayModel>,
    currentTime: Triple<Int, Int, Int>,
    modifier: Modifier = Modifier,
    onClick: (ClickEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onClick(ClickEvent.AddAlarms) },
                icon = { Icon(imageVector = Icons.Filled.Add, null) },
                text = { Text(text = stringResource(R.string.add_repeating_alarms)) }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Clock(modifier = Modifier.padding(16.dp), time = currentTime)
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
                                                group.interval.toTimeText()
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
