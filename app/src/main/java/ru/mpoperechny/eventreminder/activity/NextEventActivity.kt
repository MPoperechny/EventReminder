package ru.mpoperechny.eventreminder.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.adapters.RecyclerAdapter
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.databinding.ActivityNextEventBinding
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat


class NextEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNextEventBinding
    private val eventsViewModel: EventsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.next_events_page_toolbar_title)

        eventsViewModel.allEvents.observe(this) { eventEntities ->
            if (eventEntities.isNullOrEmpty()) showEmptyData() else updateData(eventEntities)
        }

        //debug

        eventsViewModel.deleteAll()

        //todo предусмотреть смену смену часового пояса в устройстве
        eventsViewModel.insertEvents(
            EventEntity(
                GregorianCalendar(2017, Calendar.FEBRUARY, 18).timeInMillis,
                EventEntity.BIRTHDAY,
                "234",
                "234 description"
            ),
            EventEntity(
                GregorianCalendar(2017, Calendar.FEBRUARY, 18).timeInMillis,
                EventEntity.BIRTHDAY,
                "234_2",
                "234_2 description"
            ),
            EventEntity(
                GregorianCalendar(2017, Calendar.FEBRUARY, 25).timeInMillis,
                EventEntity.BIRTHDAY,
                "567",
                "989_1"
            ),
            EventEntity(
                GregorianCalendar(2017, Calendar.FEBRUARY, 25).timeInMillis,
                EventEntity.BIRTHDAY,
                "567",
                "989_2"
            ),
            EventEntity(
                GregorianCalendar(2017, Calendar.FEBRUARY, 25).timeInMillis,
                EventEntity.BIRTHDAY,
                "567",
                "989_3"
            ),
            EventEntity(
                GregorianCalendar(2017, Calendar.MARCH, 1).timeInMillis,
                EventEntity.BIRTHDAY,
                "567111",
                "5678 description"
            )
        )



        binding.btAllEvents.setOnClickListener {
            //val intent = Intent(this@NextEventActivity, AllEventsActivity::class.java)
            //startActivity(intent)

            if (!eventsViewModel.allEvents.value.isNullOrEmpty()) {
                eventsViewModel.deleteEvent(eventsViewModel.allEvents.value!![0]);
            }

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

        if (groupedEventEntities.containsKey(true)) updateCard(groupedEventEntities.getValue(true))
        if (groupedEventEntities.containsKey(false)) updateList(groupedEventEntities.getValue(false))
    }

    private fun updateCard(nearestEventEntities: List<EventEntity>) {

        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())

        binding.nextEventCard.visibility = View.VISIBLE
        binding.eventDate.text = dateFormat.format(nearestEventEntities[0].date)
        binding.eventDaysLeft.text = getDaysLeftString(nearestEventEntities[0].daysLeft)

        val mDataSet = ArrayList(
            nearestEventEntities.map {
                it.description + " " + dateFormat.format(it.date) + " " + ". осталось: " + it.daysLeft
            }
        )
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.nextEventsRecyclerView.layoutManager = mLayoutManager
        val mAdapter = RecyclerAdapter(mDataSet)
        binding.nextEventsRecyclerView.adapter = mAdapter
    }

    //todo вынести отдельно
    private fun getDaysLeftString(days: Int): String {
        return when {
            days == 0 -> getString(R.string.today)
            days == 1 -> getString(R.string.tomorrow)
            days == 2 -> getString(R.string.two_days)
            days in 12..14 -> getString(R.string.after) + " " + days.toString() + " " + getString(R.string.days_1)
            days.toString().endsWith("2")
                    || days.toString().endsWith("3")
                    || days.toString().endsWith("4")
            -> getString(R.string.after) + " " + days.toString() + " " + getString(R.string.days_2)
            else -> getString(R.string.after) + " " + days.toString() + " " + getString(R.string.days_1)
        }
    }


    private fun updateList(eventEntities: List<EventEntity>) {

        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())

        val mDataSet = ArrayList(
            eventEntities.map {
                it.description + " " + dateFormat.format(it.date) + " " + ". осталось: " + it.daysLeft
            }
        )
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.otherEventsRecyclerView.layoutManager = mLayoutManager
        val mAdapter = RecyclerAdapter(mDataSet)
        binding.otherEventsRecyclerView.adapter = mAdapter
    }


}
