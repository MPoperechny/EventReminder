package ru.mpoperechny.eventreminder

import android.content.Context
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.utilites.daysLeftString
import ru.mpoperechny.eventreminder.utilites.timeToDateString
import java.text.SimpleDateFormat
import java.util.*

class TextEventDescription(val context: Context, event: EventEntity) {

    val person: String = event.person ?: ""

    private val dateString: String = timeToDateString(event.date)

    val fullDescriptionText: String =
        "${event.person}\n${eventTypeString(event.type)} ${dateString}"

    val shortDescriptionText: String =
        "${event.person}\n ${dateString}"

    val daysLeftMessage = daysLeftString(context, event.daysLeft)

    private fun eventTypeString(eventType: Int): String {
        return when (eventType) {
            EventEntity.BIRTHDAY -> context.getString(R.string.birthday)
            EventEntity.HOLIDAY -> context.getString(R.string.holiday)
            EventEntity.OTHER -> context.getString(R.string.other_event)
            else -> context.getString(R.string.other_event)
        }
    }
}