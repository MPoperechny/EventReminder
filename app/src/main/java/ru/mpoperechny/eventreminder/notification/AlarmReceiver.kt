package ru.mpoperechny.eventreminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("alarm receiver onReceive")

        val workManager: WorkManager = WorkManager.getInstance(context)
        val request: WorkRequest =
            OneTimeWorkRequest.Builder(NotificationWorker::class.java).build()
        workManager.enqueue(request)
    }
}