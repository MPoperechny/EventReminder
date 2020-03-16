package ru.mpoperechny.eventreminder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.mpoperechny.eventreminder.R

class AllEventsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_events)
    }
}
