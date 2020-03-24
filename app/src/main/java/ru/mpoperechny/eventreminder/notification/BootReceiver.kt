package ru.mpoperechny.eventreminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import ru.mpoperechny.eventreminder.utilites.LocalSettings

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("rebooted. alarmEnabled:${LocalSettings.isAlarmEnabled(context)}");
        Toast.makeText(context, "rebooted", Toast.LENGTH_LONG).show();

        val alarmEnabled = LocalSettings.isAlarmEnabled(context)
        if(alarmEnabled) AlarmUtils.setAlarm(context)
    }
}