package ru.mpoperechny.eventreminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import ru.mpoperechny.eventreminder.utilites.LocalSettings


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        //todo проверить перезагрузку

        println("alarm receiver onReceive")

        val alarmEnabled = LocalSettings.isAlarmEnabled(context)
        if (alarmEnabled) AlarmUtils.setAlarm(context)

        val workManager: WorkManager = WorkManager.getInstance(context)
        val request: WorkRequest =
            OneTimeWorkRequest.Builder(NotificationWorker::class.java).build()
        workManager.enqueue(request)
    }
}