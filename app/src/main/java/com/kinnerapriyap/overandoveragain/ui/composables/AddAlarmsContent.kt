package com.kinnerapriyap.overandoveragain.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kinnerapriyap.overandoveragain.ClickEvent
import com.kinnerapriyap.overandoveragain.R
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
                set(Calendar.MINUTE, get(Calendar.MINUTE) + 5)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                isLenient = false
            }
        )
    }
    var delayText by remember { mutableStateOf("5") }
    var noOfAlarms by remember { mutableStateOf("3") }
    var messageText by remember { mutableStateOf("What will I do?") }
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
            TopAppBar(
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
                label = { Text(text = "Start time") },
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
            OutlinedTextField(
                value = delayText,
                onValueChange = { delayText = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Delay Second") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = noOfAlarms,
                onValueChange = { noOfAlarms = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Number of alarms") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text(text = "Message") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    onClick(
                        ClickEvent.ScheduleRepeatingAlarm(
                            startTime.timeInMillis,
                            delayText.toLong() * 1000,
                            noOfAlarms.toInt(),
                            messageText
                        )
                    )
                }) {
                    Text(text = "Schedule")
                }
            }
        }
    }
}
