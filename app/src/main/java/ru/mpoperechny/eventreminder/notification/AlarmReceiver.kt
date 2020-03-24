package ru.mpoperechny.eventreminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.mpoperechny.eventreminder.utilites.LocalSettings

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("alarm receiver onReceive");

        val alarmEnabled = LocalSettings.isAlarmEnabled(context)
        if(alarmEnabled) AlarmUtils.setAlarm(context)
    }
}