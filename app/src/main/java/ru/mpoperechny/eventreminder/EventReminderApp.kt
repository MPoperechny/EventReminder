package ru.mpoperechny.eventreminder

import android.app.Application
import ru.mpoperechny.eventreminder.notification.AlarmUtils
import ru.mpoperechny.eventreminder.notification.NotificationUtils
import ru.mpoperechny.eventreminder.utilites.LocalSettings

class EventReminderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        println("app!!!")
        //todo сделать чтобы в день запуска срабатывало уведомление
        //todo проверить на разеых версиях
        LocalSettings.setAlarmEnabled(this, true)
        AlarmUtils.setAlarm(this)
        NotificationUtils.createNotificationChannel(this)
    }

}