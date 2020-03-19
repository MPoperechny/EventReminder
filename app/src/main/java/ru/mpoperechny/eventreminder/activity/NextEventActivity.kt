package ru.mpoperechny.eventreminder.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.adapters.EventsListAdapter
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.databinding.ActivityNextEventBinding
import ru.mpoperechny.eventreminder.utilites.daysLeftString
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat


class NextEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNextEventBinding
    private val eventsViewModel: EventsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_next_event)

        supportActionBar?.title = getString(R.string.next_events_page_toolbar_title)

        binding.otherEventsRecyclerView.adapter = EventsListAdapter(R.layout.next_events_list_item)
        binding.nextEventsRecyclerView.adapter = EventsListAdapter(R.layout.card_events_list_item)

        eventsViewModel.allEvents.observe(this) { eventEntities ->
            if (eventEntities.isNullOrEmpty()) showEmptyData() else updateData(eventEntities)
        }


        //todo предусмотреть смену смену часового пояса в устройстве

        binding.btAllEvents.setOnClickListener {
            val intent = Intent(this@NextEventActivity, AllEventsActivity::class.java)
            startActivity(intent)

            /*
            if (!eventsViewModel.allEvents.value.isNullOrEmpty()) {
                eventsViewModel.deleteEvent(eventsViewModel.allEvents.value!![0]);
            }
            */

        }
    }

    private fun showEmptyData() {
        binding.nextEventCard.visibility = View.GONE
        binding.nextText.text = getString(R.string.no_data_text)
        updateList(emptyList())
    }

    private fun updateData(eventEntities: List<EventEntity>) {

        println("update data ${eventEntities.size}")

        binding.nextText.text = getString(R.string.next_events_title)

        val sortedEventEntities = eventEntities.sortedBy { it.daysLeft }
        val groupedEventEntities =
            sortedEventEntities.groupBy { it.daysLeft == sortedEventEntities[0].daysLeft }

        val cardList =
            if (groupedEventEntities.containsKey(true)) groupedEventEntities.getValue(true) else emptyList()
        updateCard(cardList)

        val nextList =
            if (groupedEventEntities.containsKey(false)) groupedEventEntities.getValue(false) else emptyList()
        updateList(nextList)
    }

    private fun updateCard(nearestEventEntities: List<EventEntity>) {

        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())

        binding.nextEventCard.visibility = View.VISIBLE
        binding.eventDate.text = dateFormat.format(nearestEventEntities[0].date)
        binding.eventDaysLeft.text = daysLeftString(this, nearestEventEntities[0].daysLeft)

        (binding.nextEventsRecyclerView.adapter as EventsListAdapter).updateList(nearestEventEntities)
    }

    private fun eventTypeString(eventType: Int): String {
        return when (eventType) {
            EventEntity.BIRTHDAY -> getString(R.string.birthday)
            EventEntity.HOLIDAY -> getString(R.string.holiday)
            else -> getString(R.string.other_event)
        }
    }

    private fun updateList(eventEntities: List<EventEntity>) {
        (binding.otherEventsRecyclerView.adapter as EventsListAdapter).updateList(eventEntities)
    }


}
