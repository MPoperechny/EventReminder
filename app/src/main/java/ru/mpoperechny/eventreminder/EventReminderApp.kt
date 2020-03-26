package ru.mpoperechny.eventreminder

import android.app.Application
import ru.mpoperechny.eventreminder.notification.AlarmUtils
import ru.mpoperechny.eventreminder.notification.NotificationUtils
import ru.mpoperechny.eventreminder.utilites.LocalSettings

class EventReminderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationUtils.createNotificationChannel(this)

        println("Application onCreate")

        //todo вынести в пользовательские настройки
        LocalSettings.setNotificationsEnabled(this, true)
        AlarmUtils.setNotificationsAlarm(this)

        //LocalSettings.setNotificationsEnabled(this, false)
        //AlarmUtils.disableNotificationsAlarm(this)

    }
}