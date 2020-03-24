package ru.mpoperechny.eventreminder.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.activity.NextEventActivity
import ru.mpoperechny.eventreminder.utilites.LocalSettings


class AlarmReceiver : BroadcastReceiver() {

    private val notificationId: Int = 1000

    override fun onReceive(context: Context, intent: Intent) {
        println("alarm receiver onReceive");

        //todo не убирать непросмотренные сообщения
        //todo доступ к звуку по умолчанию
        //todo вид уведомления как в смс

        val alarmEnabled = LocalSettings.isAlarmEnabled(context)
        if (alarmEnabled) AlarmUtils.setAlarm(context)

        val notificationIntent = Intent(context, NextEventActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
            .setSmallIcon(R.drawable.calendar_icon)
            .setContentTitle("My notification")
            //.setContentText("Hello World! Hello World1 Hello World2 Hello World3 Hello World4 Hello World5")
            .setSound(NotificationUtils.soundUri)
            .setLights(Color.WHITE, 3000, 3000)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one апапапапапапапапапапапа line...")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }

    }
}