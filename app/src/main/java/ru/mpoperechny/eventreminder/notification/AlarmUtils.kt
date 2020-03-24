package ru.mpoperechny.eventreminder.notification

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*


object AlarmUtils {

    private const val INTENT_REQUEST_CODE = 0

    fun setAlarm(context: Context) {

        val calendar = Calendar.getInstance()
        //calendar.set(Calendar.HOUR_OF_DAY, 11)
        //calendar.set(Calendar.MINUTE, 0)

        val manager =
            context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context)

        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
        //manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 1000*60*60*4, pendingIntent2);
        //manager.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
        manager.setAlarmClock(
            //AlarmClockInfo(calendar.timeInMillis + AlarmManager.INTERVAL_DAY, pendingIntent),
            AlarmClockInfo(calendar.timeInMillis + 30 * 1000, pendingIntent),
            pendingIntent
        )
        println("alarm enabled ${Date().getHours()}:${Date().getMinutes()}")
    }

    fun disableAlarm(context: Context) {
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