package com.kinnerapriyap.overandoveragain

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import androidx.room.Room
import com.kinnerapriyap.overandoveragain.alarm.AlarmRepository
import com.kinnerapriyap.overandoveragain.alarm.AlarmScheduler
import com.kinnerapriyap.overandoveragain.alarm.DefaultAlarmRepository
import com.kinnerapriyap.overandoveragain.alarm.DefaultAlarmScheduler
import com.kinnerapriyap.overandoveragain.service.DefaultServiceViewModel
import com.kinnerapriyap.overandoveragain.service.ServiceViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

class OverAndOverAgainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@OverAndOverAgainApp)
            modules(appModule)
        }
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            setSound(defaultSoundUri, null)
            enableVibration(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

val appModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "alarm_database"
        ).build()
    }
    single<AlarmRepository> { DefaultAlarmRepository(get<AppDatabase>().alarmDao()) }
    factoryOf(::DefaultAlarmScheduler) { bind<AlarmScheduler>() }
    viewModelOf(::DefaultMainViewModel) { bind<MainViewModel>() }
    viewModelOf(::DefaultServiceViewModel) { bind<ServiceViewModel>() }
}
