package ru.mpoperechny.eventreminder.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import ru.mpoperechny.eventreminder.OperationProgressState
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.databinding.ActivityEditEventBinding
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel
import java.util.*

class EditEventActivity : AppCompatActivity() {

    private val eventsViewModel: EventsViewModel by viewModels()

    private val binding: ActivityEditEventBinding by lazy {
        DataBindingUtil.setContentView<ActivityEditEventBinding>(this, R.layout.activity_edit_event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.all_events_page_toolbar_title)

        //binding.viewModel = eventsViewModel

        binding.btSaveEvent.setOnClickListener {
            eventsViewModel.operationProgress.observe(this) {
                when (it.status) {
                    OperationProgressState.Status.FAILED ->
                        Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show()
                    OperationProgressState.Status.SUCCESS -> {
                        Toast.makeText(this, R.string.save_success, Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else -> {
                    }
                }
            }

            eventsViewModel.insertEvent(
                EventEntity(
                    GregorianCalendar(2017, Calendar.FEBRUARY, 18).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "person",
                    "person description"
                )
            )
        }
    }
}
