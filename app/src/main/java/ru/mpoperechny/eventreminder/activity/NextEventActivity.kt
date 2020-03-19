package ru.mpoperechny.eventreminder.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.adapters.EventsListAdapter
import ru.mpoperechny.eventreminder.databinding.ActivityNextEventBinding
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel


class NextEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNextEventBinding
    private val eventsViewModel: EventsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.next_events_page_toolbar_title)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_next_event)
        binding.setLifecycleOwner(this);

        binding.nextEventsRecyclerView.adapter = EventsListAdapter(R.layout.card_events_list_item)
        binding.otherEventsRecyclerView.adapter = EventsListAdapter(R.layout.next_events_list_item)

        binding.viewModel = eventsViewModel

        //todo предусмотреть смену смену часового пояса в устройстве

        binding.btAllEvents.setOnClickListener {
            startActivity(Intent(this@NextEventActivity, AllEventsActivity::class.java))
        }
    }

}
