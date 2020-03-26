package ru.mpoperechny.eventreminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.mpoperechny.eventreminder.utilites.LocalSettings


class TimeChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("TimeChangedReceiver1")
        if (intent.action == "android.intent.action.TIME_SET") {

            println("TimeChangedReceiver")

            val notificationsEnabled = LocalSettings.isNotificationsEnabled(context)
            if (notificationsEnabled) AlarmUtils.setNotificationsAlarm(context)
        }
    }
}