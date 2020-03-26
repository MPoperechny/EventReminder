package ru.mpoperechny.eventreminder.notification

import android.app.Application
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.TextEventDescription
import ru.mpoperechny.eventreminder.utilites.FactoryProvider

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        println("NotificationWorker doWork")

        val application = applicationContext as Application
        val repository = FactoryProvider.getEventsRepository(application)
        val allEvents = runBlocking { repository.getAllEvents() }

        println("allEvents $allEvents")

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

        val nearest = allEvents
            .filter { it.daysLeft == 0 || it.daysLeft == 1 }
            .sortedBy { it.daysLeft }


        println("!nearest.isNullOrEmpty() ${!nearest.isNullOrEmpty()}")

        if (!nearest.isNullOrEmpty()) {
            if (nearest.size == 1) {
                val textDescription = TextEventDescription(application, nearest[0])
                title = textDescription.daysLeftMessage
                text = textDescription.notificationMainTitle
            } else {
                title = application.getString(R.string.notification_several_events_title)
                text = nearest.joinToString(separator = "\n") { eventEntity ->
                    val descr = TextEventDescription(application, eventEntity)
                    "${descr.daysLeftMessage} ${descr.notificationMainTitle}"
                }
                expandedText = text
            }

            NotificationUtils.sendNotification(application, title, text, expandedText)
        }

        //TimeUnit.SECONDS.sleep(5);

        return Result.success()
    }

}

