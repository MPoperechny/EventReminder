package ru.mpoperechny.eventreminder.notification

import android.app.Application
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
import ru.mpoperechny.eventreminder.utilites.FactoryProvider
import ru.mpoperechny.eventreminder.utilites.LocalSettings
import ru.mpoperechny.eventreminder.utilites.timeToDateString


class AlarmReceiver : BroadcastReceiver() {

    private val notificationId: Int = 1000

    override fun onReceive(context: Context, intent: Intent) {
        println("alarm receiver onReceive");

        //todo не убирать непросмотренные сообщения
        //todo доступ к звуку и всплывающим уведомлениям по умолчанию
        //todo не всегда доступ к данным
        //todo проверить перезагрузку

        val alarmEnabled = LocalSettings.isAlarmEnabled(context)
        if (alarmEnabled) AlarmUtils.setAlarm(context)


        val repository = FactoryProvider.getEventsRepository(context.applicationContext as Application)
        var allEvents = repository.allEvents
        val dateStr = allEvents.value?.get(0)?.date?.let { timeToDateString(it) } ?: "no date"

        val notificationIntent = Intent(context, NextEventActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
            .setSmallIcon(R.drawable.calendar_icon)
            .setContentTitle("Заголовок")
            .setContentText("основной текст")
            .setDefaults(Notification.DEFAULT_ALL)
            .setVibrate(longArrayOf(0))
            .setSound(NotificationUtils.soundUri)
            .setLights(Color.WHITE, 3000, 3000)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle("Заголовок для большого текста")
                    .bigText("расширенный текст расширенный текст расширенный текст расширенный текст расширенный текст расширенный текст расширенный текст.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }

    }
}