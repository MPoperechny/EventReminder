package ru.mpoperechny.eventreminder.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.adapters.EventsListAdapter
import ru.mpoperechny.eventreminder.databinding.ActivityAllEventsBinding
import ru.mpoperechny.eventreminder.utilites.timeToDateString
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel


class AllEventsActivity : AppCompatActivity() {

    //todo сортировать список
    //todo использовать один репозитоий
    private val eventsViewModel: EventsViewModel by viewModels()

    private val binding: ActivityAllEventsBinding by lazy {
        DataBindingUtil.setContentView<ActivityAllEventsBinding>(this, R.layout.activity_all_events)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.all_events_page_toolbar_title)

        //binding.viewModel = eventsViewModel

        val allEventsAdapter = EventsListAdapter(R.layout.all_events_list_item)
        allEventsAdapter.onItemClick = clickListener
        binding.adapter = allEventsAdapter

        eventsViewModel.allEvents.observe(this) { it.let(allEventsAdapter::updateList) }

        binding.btAddEvent.setOnClickListener {
            startActivity(Intent(this@AllEventsActivity, EditEventActivity::class.java))
        }
    }


    private val clickListener: ((position: Int, type: Int) -> Unit)? =
        { pos, _ ->
            val eventDesc =
                eventsViewModel.allEvents.value?.get(pos)?.date?.let { timeToDateString(it) }

            val builder = AlertDialog.Builder(this)
            builder.setMessage(
                "${getString(R.string.select_action_for)} $eventDesc"
            )
            builder.setPositiveButton(R.string.delete) { _, _ -> confirmDelete(pos) }
            builder.setNegativeButton(R.string.edit, null)
            builder.setNeutralButton(android.R.string.no, null)
            builder.show()
        }

    private fun confirmDelete(pos: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("${getString(R.string.delete)}?")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            eventsViewModel.allEvents.value?.let {
                if (it.size > pos) eventsViewModel.deleteEvent(it[pos])
            }
        }
        builder.setNegativeButton(android.R.string.no, null)
        builder.show()
    }

}
