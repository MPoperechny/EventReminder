package ru.mpoperechny.eventreminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.mpoperechny.eventreminder.utilites.LocalSettings

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("rebooted")

        val notificationsEnabled = LocalSettings.isNotificationsEnabled(context)
        if (notificationsEnabled) AlarmUtils.setNotificationsAlarm(context)
    }
}