package com.kinnerapriyap.overandoveragain.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kinnerapriyap.overandoveragain.ClickEvent
import com.kinnerapriyap.overandoveragain.R
import com.kinnerapriyap.overandoveragain.utils.DEFAULT_DELAY
import com.kinnerapriyap.overandoveragain.utils.IntervalType
import com.kinnerapriyap.overandoveragain.utils.convertToDisplayTime
import com.kinnerapriyap.overandoveragain.utils.getLocale
import com.kinnerapriyap.overandoveragain.utils.toTimeNumber
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
    var delayTimeInMillis by remember { mutableLongStateOf(DEFAULT_DELAY) }
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
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.add_alarms))
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
                        onClick(
                            ClickEvent.ScheduleRepeatingAlarm(
                                startTime.timeInMillis,
                                delayTimeInMillis,
                                noOfAlarms.intValue.toInt(),
                                messageText
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
            Row(verticalAlignment = Alignment.Bottom) {
                OutlinedTextField(
                    modifier = Modifier.width(136.dp),
                    value = delayTimeInMillis.toTimeNumber(intervalType).toString(),
                    onValueChange = { delayTimeInMillis = it.toLong() * intervalType.millis },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = stringResource(R.string.interval)) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                ExposedDropdownMenuBox(
                    modifier = Modifier.width(136.dp),
                    expanded = intervalTypeExpanded,
                    onExpandedChange = { intervalTypeExpanded = it },
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(PrimaryNotEditable),
                        readOnly = true,
                        value = intervalType.name,
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
                                text = { Text(type.name) },
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Counter(
                title = stringResource(R.string.number_of_alarms),
                noOfAlarms = noOfAlarms
            )
            Spacer(modifier = Modifier.height(8.dp))
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
