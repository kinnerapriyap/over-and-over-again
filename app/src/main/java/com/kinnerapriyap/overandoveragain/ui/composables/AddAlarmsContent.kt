package com.kinnerapriyap.overandoveragain.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kinnerapriyap.overandoveragain.ClickEvent
import com.kinnerapriyap.overandoveragain.R
import com.kinnerapriyap.overandoveragain.utils.DAY_MILLIS
import com.kinnerapriyap.overandoveragain.utils.DEFAULT_DELAY
import com.kinnerapriyap.overandoveragain.utils.IntervalType
import com.kinnerapriyap.overandoveragain.utils.convertToDisplayTime
import com.kinnerapriyap.overandoveragain.utils.getLocale
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmsContent(
    modifier: Modifier = Modifier,
    onClick: (ClickEvent) -> Unit
) {
    var openTimePickerDialog by remember { mutableStateOf(false) }
    var startTime by remember {
        mutableStateOf(
            Calendar.getInstance().apply {
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                isLenient = false
                timeInMillis = timeInMillis + 5 * 60 * 1000
            }
        )
    }
    var delayTime = remember { mutableIntStateOf(DEFAULT_DELAY) }
    var noOfAlarms = remember { mutableIntStateOf(3) }
    var messageText by remember { mutableStateOf("~") }
    val options = IntervalType.entries
    var intervalTypeExpanded by remember { mutableStateOf(false) }
    var intervalType by remember { mutableStateOf(options[0]) }
    if (openTimePickerDialog) {
        TimePickerDial(
            time = startTime,
            onConfirm = {
                startTime = startTime.apply {
                    set(Calendar.HOUR_OF_DAY, it.hour)
                    set(Calendar.MINUTE, it.minute)
                }
                openTimePickerDialog = false
            }, onDismiss = {
                openTimePickerDialog = false
            }
        )
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.add_repeating_alarms))
                },
                navigationIcon = {
                    IconButton(onClick = { onClick(ClickEvent.Back) }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(R.string.clear)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val startInMillis = if (startTime.after(Calendar.getInstance())) {
                            startTime.timeInMillis
                        } else {
                            startTime.timeInMillis + DAY_MILLIS
                        }
                        onClick(
                            ClickEvent.ScheduleRepeatingAlarm(
                                time = startInMillis,
                                delay = delayTime.intValue * intervalType.millis,
                                count = noOfAlarms.intValue.toInt(),
                                message = messageText
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.clear)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Clock(
                modifier = Modifier.padding(32.dp),
                time = Triple(startTime.get(Calendar.HOUR), startTime.get(Calendar.MINUTE), 0),
                showSeconds = false
            )
            OutlinedTextField(
                value = startTime.timeInMillis.convertToDisplayTime(locale = getLocale()),
                onValueChange = { },
                label = { Text(text = stringResource(R.string.start_time)) },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { openTimePickerDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null,
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.i_need))
                CounterButton(value = noOfAlarms, modifier = Modifier.padding(16.dp))
                Text(text = stringResource(R.string.alarms))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stringResource(R.string.every))
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CounterButton(value = delayTime)
                Spacer(modifier = Modifier.width(16.dp))
                ExposedDropdownMenuBox(
                    modifier = Modifier.width(136.dp),
                    expanded = intervalTypeExpanded,
                    onExpandedChange = { intervalTypeExpanded = it },
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(PrimaryNotEditable),
                        readOnly = true,
                        value = pluralStringResource(intervalType.stringRes, delayTime.intValue),
                        onValueChange = { },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = intervalTypeExpanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = intervalTypeExpanded,
                        onDismissRequest = { intervalTypeExpanded = false }
                    ) {
                        options.forEach { type ->
                            DropdownMenuItem(
                                onClick = {
                                    intervalType = type
                                    intervalTypeExpanded = false
                                },
                                text = {
                                    Text(
                                        pluralStringResource(
                                            type.stringRes,
                                            delayTime.intValue
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text(text = stringResource(R.string.alarm_message)) },
            )
        }
    }
}

@Preview
@Composable
private fun AddAlarmsContentPreview() {
    AddAlarmsContent(onClick = {})
}
