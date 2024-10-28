package com.kinnerapriyap.overandoveragain

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kinnerapriyap.overandoveragain.alarm.RepeatingAlarmRequest
import com.kinnerapriyap.overandoveragain.ui.composables.AddAlarmsContent
import com.kinnerapriyap.overandoveragain.ui.composables.ListContent
import com.kinnerapriyap.overandoveragain.ui.composables.RepeatingAlarmContent
import com.kinnerapriyap.overandoveragain.ui.theme.OverAndOverAgainTheme
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModel<DefaultMainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            OverAndOverAgainTheme {
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = Screen.List.route,
                ) {
                    composable(route = Screen.List.route) {
                        val alarms by mainViewModel.repeatingAlarms
                            .collectAsStateWithLifecycle(emptyList())
                        val currentTime by mainViewModel.currentTime.collectAsStateWithLifecycle()
                        ListContent(
                            repeatingAlarms = alarms.toImmutableList(),
                            currentTime = currentTime,
                            onClick = { it.onClickEvent(navController) }
                        )
                    }
                    composable(route = Screen.AddAlarms.route) {
                        AddAlarmsContent(
                            onClick = { it.onClickEvent(navController) }
                        )
                    }
                    composable(
                        route = Screen.RepeatingAlarm.route + "?id={$ARGUMENT_ID}",
                        arguments = listOf(
                            navArgument(ARGUMENT_ID) {
                                type = NavType.StringType
                                nullable = false
                            }
                        )
                    ) {
                        val id = it.arguments?.getString(ARGUMENT_ID) ?: ""
                        RepeatingAlarmContent(
                            onClick = { it.onClickEvent(navController) }
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("BatteryLife")
    override fun onResume() {
        super.onResume()
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        } else if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        } else if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }

    private fun ClickEvent.onClickEvent(navController: NavController) = when (this) {
        is ClickEvent.ScheduleRepeatingAlarm -> {
            val repeatingAlarmRequest = RepeatingAlarmRequest(
                time = time,
                delay = delay,
                count = count,
                message = message
            )
            mainViewModel.scheduleRepeatingAlarm(repeatingAlarmRequest)
            navController.popBackStack()
        }

        ClickEvent.AddAlarms -> navController.navigate(Screen.AddAlarms.route)

        ClickEvent.Back -> navController.popBackStack()
    }
}

internal const val CHANNEL_ID = "alarm_id"
internal const val ARGUMENT_ID = "id"

sealed class Screen(val route: String) {
    object List : Screen("list_screen")
    object AddAlarms : Screen("add_alarms_screen")
    object RepeatingAlarm : Screen("repeating_alarm_screen")
}

sealed interface ClickEvent {
    data class ScheduleRepeatingAlarm(
        val time: Long,
        val delay: Long,
        val count: Int,
        val message: String
    ) : ClickEvent

    data object AddAlarms : ClickEvent

    data object Back : ClickEvent
}
