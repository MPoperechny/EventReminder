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
import ru.mpoperechny.eventreminder.TextEventDescription
import ru.mpoperechny.eventreminder.activity.NextEventActivity
import ru.mpoperechny.eventreminder.utilites.FactoryProvider
import ru.mpoperechny.eventreminder.utilites.LocalSettings


class AlarmReceiver : BroadcastReceiver() {

    private val notificationId: Int = 1000

    override fun onReceive(context: Context, intent: Intent) {
        println("alarm receiver onReceive");

        //todo доступ к звуку и всплывающим уведомлениям по умолчанию
        //todo не всегда доступ к данным
        //todo проверить перезагрузку

        val alarmEnabled = LocalSettings.isAlarmEnabled(context)
        if (alarmEnabled) AlarmUtils.setAlarm(context)


        val repository =
            FactoryProvider.getEventsRepository(context.applicationContext as Application)
        val allEvents = repository.allEvents

        /*
        если событие одно:
        заголовок - сколько осталось дней
        сообщение - описание события
        расширенный текст не используется

        если событий несколько:
        заголовок - "Ближайшие события"
        сообщение - список [сколько дней осталось + описание события]
        расширенный текст - список [сколько дней осталось + описание события]
        */

        var title = ""
        var text = ""
        var expandedText: String? = null

        val nearest = allEvents.value?.
            filter { it.daysLeft == 0 || it.daysLeft == 1 }?.
            sortedBy { it.daysLeft }


        if (!nearest.isNullOrEmpty()) {
            if (nearest.size == 1) {
                val textDescription = TextEventDescription(context, nearest[0])
                title = textDescription.daysLeftMessage
                text = textDescription.notificationMainTitle
            } else {
                title = context.getString(R.string.notification_several_events_title)
                text = nearest.joinToString(separator = "\n") { eventEntity ->
                    val descr = TextEventDescription(context, eventEntity)
                    "${descr.daysLeftMessage} ${descr.notificationMainTitle}"
                }
                expandedText = text
            }
        }

        if (!nearest.isNullOrEmpty()) sendNotification(context, title, text, expandedText)
    }

    private fun sendNotification(
        context: Context,
        title: String,
        text: String,
        expandedText: String?
    ) {
        val notificationIntent = Intent(context, NextEventActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
            .setSmallIcon(R.drawable.calendar_icon)
            .setContentTitle(title)
            .setContentText(text)
            .setDefaults(Notification.DEFAULT_ALL)
            .setVibrate(longArrayOf(0))
            .setSound(NotificationUtils.soundUri)
            .setLights(Color.WHITE, 3000, 3000)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        expandedText?.let { builder.setStyle(NotificationCompat.BigTextStyle().bigText(it)) }


        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}