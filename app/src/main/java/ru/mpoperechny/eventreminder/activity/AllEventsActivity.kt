package ru.mpoperechny.eventreminder.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.adapters.EventsListAdapter
import ru.mpoperechny.eventreminder.databinding.ActivityAllEventsBinding
import ru.mpoperechny.eventreminder.utilites.FactoryProvider.provideViewModelFactory
import ru.mpoperechny.eventreminder.utilites.timeToDateString
import ru.mpoperechny.eventreminder.viewmodel.AllEventsViewModel


class AllEventsActivity : AppCompatActivity() {


    private val viewModel: AllEventsViewModel by viewModels {
        provideViewModelFactory(application)
    }

    private val binding: ActivityAllEventsBinding by lazy {
        DataBindingUtil.setContentView<ActivityAllEventsBinding>(this, R.layout.activity_all_events)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.all_events_page_toolbar_title)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.lifecycleOwner = this

        //binding.viewModel = eventsViewModel

        val allEventsAdapter = EventsListAdapter(R.layout.all_events_list_item)
        allEventsAdapter.onItemClick = clickListener
        binding.adapter = allEventsAdapter

        val mDividerItemDecoration =
            DividerItemDecoration(binding.rvEventsList.context, DividerItemDecoration.VERTICAL)
        binding.rvEventsList.addItemDecoration(mDividerItemDecoration)

        viewModel.allEventsSortedByDayOfYear.observe(this) { it.let(allEventsAdapter::updateList) }

        binding.btAddEvent.setOnClickListener {
            val intent = Intent(this@AllEventsActivity, EditEventActivity::class.java)
            startActivity(intent)
        }

        binding.btDeleteAll.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("${getString(R.string.delete_all)}?")
            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                viewModel.deleteAll()
            }
            builder.setNegativeButton(android.R.string.no, null)
            builder.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val clickListener: ((position: Int, type: Int) -> Unit)? =
        { pos, _ ->
            val eventDesc =
                viewModel.allEventsSortedByDayOfYear.value?.get(pos)?.date?.let { timeToDateString(it) }

            val builder = AlertDialog.Builder(this)
            builder.setMessage(
                "${getString(R.string.select_action_for)} $eventDesc"
            )
            builder.setPositiveButton(R.string.delete) { _, _ -> confirmDelete(pos) }
            builder.setNegativeButton(R.string.edit, { _, _ -> editEvent(pos) })
            builder.setNeutralButton(android.R.string.no, null)
            builder.show()
        }

    private fun editEvent(pos: Int) {
        val intent = Intent(this@AllEventsActivity, EditEventActivity::class.java)
        viewModel.allEventsSortedByDayOfYear.value?.get(pos)?.id.let {
            intent.putExtra(EditEventActivity.EVENT_ID, it)
        }
        startActivity(intent)
    }

    private fun confirmDelete(pos: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("${getString(R.string.delete)}?")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            viewModel.allEventsSortedByDayOfYear.value?.let {
                if (it.size > pos) viewModel.deleteEvent(it[pos])
            }
        }
        builder.setNegativeButton(android.R.string.no, null)
        builder.show()
    }

}
