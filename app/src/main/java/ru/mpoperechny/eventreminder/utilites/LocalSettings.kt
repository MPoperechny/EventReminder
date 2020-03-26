package ru.mpoperechny.eventreminder.utilites

import android.content.Context


object LocalSettings {

    private const val SETTINGS = "settings"
    private const val NOTIFICATIONS_ENABLED_KEY = "notifications_enabled_key"

    fun setNotificationsEnabled(context: Context, value: Boolean) {
        val sPref =
            context.applicationContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        val ed = sPref.edit()
        ed.putBoolean(NOTIFICATIONS_ENABLED_KEY, value)
        ed.apply()
    }

    fun isNotificationsEnabled(context: Context): Boolean {
        val sPref = context.applicationContext
            .getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        return sPref.getBoolean(NOTIFICATIONS_ENABLED_KEY, false)
    }
}