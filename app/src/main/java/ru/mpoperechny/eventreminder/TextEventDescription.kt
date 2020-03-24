package ru.mpoperechny.eventreminder

import android.content.Context
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.utilites.daysLeftString
import ru.mpoperechny.eventreminder.utilites.timeToDateString
import java.text.SimpleDateFormat
import java.util.*

class TextEventDescription(val context: Context, event: EventEntity) {

    val dateString: String = timeToDateString(event.date)

    val eventType = eventTypeString(event.type)

    val mainTitle = mainTitleString(event)

    private fun mainTitleString(event: EventEntity): String {
        val lineBreak = if (event.person != null && event.description != null) "\n" else ""
        return when (event.type) {
            EventEntity.BIRTHDAY -> "${event.person}$lineBreak${event.description ?: ""}"
            EventEntity.HOLIDAY -> "${event.description}$lineBreak${event.person ?: ""}"
            EventEntity.OTHER -> "${event.description ?: ""}$lineBreak${event.person ?: ""}"
            else -> "${event.description ?: ""}$lineBreak${event.person ?: ""}"
        }
    }

    private fun eventTypeString(eventType: Int): String {
        return when (eventType) {
            EventEntity.BIRTHDAY -> context.getString(R.string.birthday)
            EventEntity.HOLIDAY -> context.getString(R.string.holiday)
            EventEntity.OTHER -> context.getString(R.string.other_event)
            else -> context.getString(R.string.other_event)
        }
    }

    /*
    val person: String = event.person ?: ""

    val fullDescriptionText: String =
        "${event.person}\n${eventTypeString(event.type)} $dateString"

    val shortDescriptionText: String = "${event.person}\n $dateString"

    val daysLeftMessage = daysLeftString(context, event.daysLeft)
     */
}