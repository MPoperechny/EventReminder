package ru.mpoperechny.eventreminder.notification

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*


object AlarmUtils {

    private const val INTENT_REQUEST_CODE = 0

    fun setNotificationsAlarm(context: Context) {

        //println("setNotificationsAlarm")

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 11)
        calendar.set(Calendar.MINUTE, 0)

        val now = Calendar.getInstance().timeInMillis
        with(calendar) {if (now >= timeInMillis) add(Calendar.HOUR_OF_DAY, 24)}

        val manager =
            context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context)

        manager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )


        //1 minute interval
        /*
        manager.cancel(pendingIntent)
        val testCalendar = Calendar.getInstance()
        testCalendar.set(Calendar.SECOND, 30)
        if (Calendar.getInstance().timeInMillis >= testCalendar.timeInMillis) {
            testCalendar.add(Calendar.MINUTE, 1)
        }
        manager.setRepeating(AlarmManager.RTC_WAKEUP, testCalendar.timeInMillis, 60*1000, pendingIntent)
        */
    }

    fun disableNotificationsAlarm(context: Context) {
        val manager =
            context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context)
        manager.cancel(pendingIntent)
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}