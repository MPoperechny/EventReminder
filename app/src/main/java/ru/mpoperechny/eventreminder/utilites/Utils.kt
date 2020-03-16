package ru.mpoperechny.eventreminder.utilites

import android.content.Context
import ru.mpoperechny.eventreminder.R

fun daysLeftString(context:Context, days: Int): String {
    return when {
        days == 0 -> context.getString(R.string.today)
        days == 1 -> context.getString(R.string.tomorrow)
        days == 2 -> context.getString(R.string.two_days)
        days in 12..14 -> context.getString(R.string.after) + " " + days.toString() + " " + context.getString(R.string.days_1)
        days.toString().endsWith("2")
                || days.toString().endsWith("3")
                || days.toString().endsWith("4")
        -> context.getString(R.string.after) + " " + days.toString() + " " + context.getString(R.string.days_2)
        else -> context.getString(R.string.after) + " " + days.toString() + " " + context.getString(R.string.days_1)
    }
}