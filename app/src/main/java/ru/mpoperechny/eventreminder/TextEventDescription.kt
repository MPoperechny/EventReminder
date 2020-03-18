package ru.mpoperechny.eventreminder

import android.content.Context
import android.content.res.Resources
import ru.mpoperechny.eventreminder.database.EventEntity
import java.text.SimpleDateFormat
import java.util.*

class TextEventDescription(val context: Context, event: EventEntity) {

    val dateString: String = dateFormat.format(event.date)
    val fullDescriptionText: String =
        "${event.person}\n${eventTypeString(event.type)} ${dateString} ${daysLeftString(event.daysLeft)}"

    private fun eventTypeString(eventType: Int): String {
        return when (eventType) {
            EventEntity.BIRTHDAY -> context.getString(R.string.birthday)
            EventEntity.HOLIDAY -> context.getString(R.string.holiday)
            EventEntity.OTHER -> context.getString(R.string.other_event)
            else -> context.getString(R.string.other_event)
        }
    }

    private fun daysLeftString(days: Int): String {
        return when {
            days == 0 -> context.getString(R.string.today)
            days == 1 -> context.getString(R.string.tomorrow)
            days == 2 -> context.getString(R.string.two_days)
            days in 12..14 -> context.getString(R.string.after) + " " + days.toString() + " " + context.getString(
                R.string.days_1
            )
            days.toString().endsWith("2")
                    || days.toString().endsWith("3")
                    || days.toString().endsWith("4")
            -> context.getString(R.string.after) + " " + days.toString() + " " + context.getString(R.string.days_2)
            else -> context.getString(R.string.after) + " " + days.toString() + " " + context.getString(
                R.string.days_1
            )
        }
    }

    companion object {
        private val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
    }
}