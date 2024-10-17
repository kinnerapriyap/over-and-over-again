package com.kinnerapriyap.overandoveragain.ui.composables

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kinnerapriyap.overandoveragain.ClickEvent
import java.time.LocalDateTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(modifier: Modifier = Modifier, onClick: (ClickEvent) -> Unit) {
    var hasNotificationPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasNotificationPermission = it }
    )
    var openTimePickerDialog by remember { mutableStateOf(false) }
    var startTime by remember {
        mutableStateOf(
            TimePickerState(
                initialHour = LocalDateTime.now().hour,
                initialMinute = LocalDateTime.now().minute + 1,
                is24Hour = true,
            )
        )
    }
    var delayText by remember { mutableStateOf("5") }
    var noOfAlarms by remember { mutableStateOf("5") }
    var messageText by remember { mutableStateOf("eh") }
    if (openTimePickerDialog) {
        TimePickerDial(
            onConfirm = {
                startTime = it
                openTimePickerDialog = false
            }, onDismiss = {
                openTimePickerDialog = false
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = "${startTime.hour}:${startTime.minute}",
            onValueChange = { },
            label = { Text(text = "Start time") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { openTimePickerDialog = true }) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_my_calendar),
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
                if (!hasNotificationPermission) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, startTime.hour)
                    cal.set(Calendar.MINUTE, startTime.minute)
                    cal.isLenient = false
                    onClick(
                        ClickEvent.ScheduleAlarm(
                            cal,
                            delayText.toLong() * 1000,
                            noOfAlarms.toInt(),
                            messageText
                        )
                    )
                }
            }) {
                Text(text = "Schedule")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onClick(ClickEvent.CancelAlarm) }) {
                Text(text = "Cancel")
            }
        }
    }
}
