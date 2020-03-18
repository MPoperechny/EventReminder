package ru.mpoperechny.eventreminder.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.adapters.AllEventsListAdapter
import ru.mpoperechny.eventreminder.databinding.ActivityAllEventsBinding
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel


class AllEventsActivity : AppCompatActivity() {

    //todo использовать один репозитоий
    private val eventsViewModel: EventsViewModel by viewModels()

    private val binding: ActivityAllEventsBinding by lazy {
        DataBindingUtil.setContentView<ActivityAllEventsBinding>(this, R.layout.activity_all_events)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.all_events_page_toolbar_title)

        //binding.viewModel = eventsViewModel

        val allEventsAdapter = AllEventsListAdapter()
        binding.adapter = allEventsAdapter

        eventsViewModel.allEvents.observe(this) { it.let(allEventsAdapter::updateList)}
    }


}
