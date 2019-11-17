package ru.mpoperechny.eventreminder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.adapters.RecyclerAdapter
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.databinding.ActivityNextEventBinding
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel
import java.util.ArrayList

class NextEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNextEventBinding
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Log.d("rlf_app", "actionBar " + supportActionBar)
        supportActionBar?.title = getString(R.string.next_events_page_toolbar_title)
        mRecyclerView = binding.nextEventsRecyclerView

        eventsViewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        eventsViewModel.allEvents?.observe(this, object : Observer<List<EventEntity>> {
            override fun onChanged(@Nullable eventEntities: List<EventEntity>?) {
                updateList()

                if (eventEntities != null && eventEntities.isNotEmpty()) {
                    binding.eventName.setText(eventEntities[0].description)
                } else {
                    binding.eventName.setText(R.string.no_data_text)
                }
            }

        })


        eventsViewModel.insertEvents(
            EventEntity(1, EventEntity.BIRTHDAY, "234", "234 description"),
            EventEntity(2, EventEntity.BIRTHDAY, "567", "567 description")
        )


        /*
        val allEventsBut = findViewById(R.id.bt_all_events)
        allEventsBut.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@NextEventActivity, AllEventsActivity::class.java)
            startActivity(intent)

            /*
                    if (eventsViewModel.getAllEvents().getValue().size() > 0) {
                        eventsViewModel.deleteEvent(eventsViewModel.getAllEvents().getValue().get(0));
                    }
                    */
        })

         */
    }


    private fun updateList() {

        Log.d("rlf_app", "updateList")

        val mDataSet = ArrayList<String?>()

        val allEvents = eventsViewModel.allEvents?.value

        if (allEvents != null && allEvents!!.size > 0) {

            Log.d("rlf_app", "allEvents.size():" + allEvents!!.size)

            for (i in allEvents!!.indices) {
                Log.d(
                    "rlf_app",
                    "allEvents.get(i).getDescription():" + allEvents!!.get(i).description
                )
                mDataSet.add(i, allEvents.get(i).description)
            }
        }

        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false)
        //mRecyclerView.setPreserveFocusAfterLayout(false);
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager)
        val mAdapter = RecyclerAdapter(mDataSet)
        mRecyclerView.setAdapter(mAdapter)

        return
    }


}
