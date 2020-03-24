package ru.mpoperechny.eventreminder.utilites

import android.content.Context


object LocalSettings {

    private const val SETTINGS = "settings"
    private const val ALARM_ENABLED_KEY = "alarm_enabled_key"

    fun setAlarmEnabled(context: Context, value: Boolean) {
        val sPref =
            context.applicationContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        val ed = sPref.edit()
        ed.putBoolean(ALARM_ENABLED_KEY, value)
        ed.apply()
    }

    fun isAlarmEnabled(context: Context): Boolean {
        val sPref = context.applicationContext
            .getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        return sPref.getBoolean(ALARM_ENABLED_KEY, false)
    }
}