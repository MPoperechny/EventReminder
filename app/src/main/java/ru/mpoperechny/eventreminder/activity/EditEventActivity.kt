package ru.mpoperechny.eventreminder.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import ru.mpoperechny.eventreminder.EntityValidator
import ru.mpoperechny.eventreminder.EventEntityFormatter
import ru.mpoperechny.eventreminder.OperationProgressState
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.databinding.ActivityEditEventBinding
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel
import java.util.*

class EditEventActivity : AppCompatActivity() {

    //todo высота лейаута
    //todo отступы

    private val eventEntityFormatter = EventEntityFormatter()
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
                    OperationProgressState.Status.RUNNING -> {
                    }
                }
            }

            val setDataResult = eventEntityFormatter.setData(
                person = binding.etName.text.toString(),
                description = binding.etDescription.text.toString()
            )
            //todo get data from spinner

            if(setDataResult == EntityValidator.Result.SUCCESS){
                eventsViewModel.insertEvent(eventEntityFormatter.currentEventEntity)
            }else{
                Toast.makeText(this, setDataResult.name, Toast.LENGTH_LONG).show()
            }

        }

        binding.btDayPicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    //println("selected $dayOfMonth $monthOfYear $year")
                    eventEntityFormatter.setData(
                        date = GregorianCalendar(
                            year,
                            monthOfYear,
                            dayOfMonth
                        ).timeInMillis
                    )
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }
}
