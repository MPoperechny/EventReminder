package ru.mpoperechny.eventreminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.mpoperechny.eventreminder.databinding.ActivityNextEventBinding

class NextEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNextEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Log.d("rlf_app", "actionBar " + supportActionBar)
        supportActionBar?.title = getString(R.string.next_events_page_toolbar_title)
    }
}
