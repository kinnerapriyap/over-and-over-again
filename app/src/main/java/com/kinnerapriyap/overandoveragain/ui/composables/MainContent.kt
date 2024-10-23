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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import com.kinnerapriyap.overandoveragain.ClickEvent
import com.kinnerapriyap.overandoveragain.alarm.AlarmItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    alarms: List<AlarmItem>,
    modifier: Modifier = Modifier,
    onClick: (ClickEvent) -> Unit
) {
    var hasNotificationPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasNotificationPermission = it }
    )
    var openTimePickerDialog by remember { mutableStateOf(false) }
    var startTime by remember {
        mutableStateOf(
            Calendar.getInstance().apply {
                set(Calendar.MINUTE, get(Calendar.MINUTE) + 1)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                isLenient = false
            }
        )
    }
    var delayText by remember { mutableStateOf("5") }
    var noOfAlarms by remember { mutableStateOf("5") }
    var messageText by remember { mutableStateOf("eh") }
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
    LazyColumn(modifier = modifier) {
        items(alarms.groupBy { it.repeatingAlarmId }.toList()) { (_, group) ->
            val times = group.map { it.time.convertToDisplayTime(locale = getLocale()) }
            Text(text = times.joinToString())
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = startTime.timeInMillis.convertToDisplayTime(locale = getLocale()),
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
                    onClick(
                        ClickEvent.ScheduleRepeatingAlarm(
                            startTime.timeInMillis,
                            delayText.toLong() * 1000,
                            noOfAlarms.toInt(),
                            messageText
                        )
                    )
                }
            }) {
                Text(text = "Schedule")
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
