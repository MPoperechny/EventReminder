package ru.mpoperechny.eventreminder.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.adapters.RecyclerAdapter
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.databinding.ActivityNextEventBinding
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel

class NextEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNextEventBinding
    private lateinit var eventsViewModel: EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.next_events_page_toolbar_title)

        eventsViewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        eventsViewModel.allEvents.observe(this, object : Observer<List<EventEntity>> {
            override fun onChanged(@Nullable eventEntities: List<EventEntity>?) {
                if (eventEntities.isNullOrEmpty()) showEmptyData() else updateData(eventEntities)
            }
        })

        //debug
        eventsViewModel.deleteAll()

        eventsViewModel.insertEvents(
            EventEntity(1, EventEntity.BIRTHDAY, "234", "234 description"),
            EventEntity(2, EventEntity.BIRTHDAY, "567", "989"),
            EventEntity(2, EventEntity.BIRTHDAY, "567", "5678 description")
        )


        binding.btAllEvents.setOnClickListener(View.OnClickListener {
            //val intent = Intent(this@NextEventActivity, AllEventsActivity::class.java)
            //startActivity(intent)

            if (!eventsViewModel.allEvents.value.isNullOrEmpty()) {
                eventsViewModel.deleteEvent(eventsViewModel.allEvents.value!![0]);
            }

        })

    }

    private fun showEmptyData() {
        Log.d("rlf_app", "showEmptyData")
        binding.nextEventCard.visibility = View.GONE
        binding.nextText.text = getString(R.string.no_data_text)
        updateList(emptyList())
    }

    private fun updateData(eventEntities: List<EventEntity>) {
        Log.d("rlf_app", "updateData")

        binding.nextText.text = getString(R.string.next_events_title)

        updateCard(eventEntities[0])
        updateList(eventEntities)
    }

    private fun updateCard(eventEntity: EventEntity) {
        binding.nextEventCard.visibility = View.VISIBLE
        binding.eventName.text = eventEntity.description
    }

    private fun updateList(eventEntities: List<EventEntity>) {
        val mDataSet = ArrayList(eventEntities.map { it.description })
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.nextEventsRecyclerView.layoutManager = mLayoutManager
        val mAdapter = RecyclerAdapter(mDataSet)
        binding.nextEventsRecyclerView.adapter = mAdapter
    }


}
