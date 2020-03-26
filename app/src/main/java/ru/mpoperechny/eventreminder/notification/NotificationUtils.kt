package ru.mpoperechny.eventreminder.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.activity.NextEventActivity

object NotificationUtils {

    private const val CHANNEL_ID = "ru.mpoperechny.eventreminder.notification.CHANNEL_ID"
    private const val NOTIFICATION_ID: Int = 1000

    private val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            channel.setSound(soundUri, audioAttributes)

            val notificationManager: NotificationManager =
                context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    //todo доступ к звуку и всплывающим уведомлениям по умолчанию

    fun sendNotification(
        context: Context,
        title: String,
        text: String,
        expandedText: String?
    ) {
        val notificationIntent = Intent(context, NextEventActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.calendar_icon)
            .setContentTitle(title)
            .setContentText(text)
            .setDefaults(Notification.DEFAULT_ALL)
            .setVibrate(longArrayOf(0))
            .setSound(soundUri)
            .setLights(Color.WHITE, 3000, 3000)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        expandedText?.let { builder.setStyle(NotificationCompat.BigTextStyle().bigText(it)) }


        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}