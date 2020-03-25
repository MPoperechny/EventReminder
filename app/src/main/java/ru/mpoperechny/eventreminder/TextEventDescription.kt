package ru.mpoperechny.eventreminder

import android.content.Context
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.utilites.daysLeftString
import ru.mpoperechny.eventreminder.utilites.timeToDateString

class TextEventDescription(val context: Context, val event: EventEntity) {

    val dateString: String = timeToDateString(event.date)

    val eventType = eventTypeString()

    val mainTitle = mainTitleString()

    val daysLeftMessage = daysLeftString(context, event.daysLeft)

    val notificationMainTitle: String
        get() = notificationMainString()

    private fun notificationMainString(): String {
        val separator = if (event.person != null && event.description != null) " " else ""
        return when (event.type) {
            EventEntity.BIRTHDAY -> "$eventType ${event.person}"
            else -> "${event.description ?: ""}$separator${event.person ?: ""}"
        }
    }

    private fun mainTitleString(): String {
        val possibleLineBreak = if (event.person != null && event.description != null) "\n" else ""
        return when (event.type) {
            EventEntity.BIRTHDAY -> "${event.person}$possibleLineBreak${event.description ?: ""}"
            EventEntity.HOLIDAY -> "${event.description}$possibleLineBreak${event.person ?: ""}"
            EventEntity.OTHER -> "${event.description ?: ""}$possibleLineBreak${event.person ?: ""}"
            else -> "${event.description ?: ""}$possibleLineBreak${event.person ?: ""}"
        }
    }

    private fun eventTypeString(): String {
        return when (event.type) {
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


     */
}