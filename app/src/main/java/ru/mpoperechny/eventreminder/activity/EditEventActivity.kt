package ru.mpoperechny.eventreminder.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import ru.mpoperechny.eventreminder.*
import ru.mpoperechny.eventreminder.databinding.ActivityEditEventBinding
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModel
import java.util.*


class EditEventActivity : AppCompatActivity() {

    //todo высота лейаута
    //todo отступы

    private val eventsViewModel: EventsViewModel by viewModels()
    private val binding: ActivityEditEventBinding by lazy {
        DataBindingUtil.setContentView<ActivityEditEventBinding>(this, R.layout.activity_edit_event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.all_events_page_toolbar_title)

        //binding.viewModel = eventsViewModel

        eventsViewModel.saveProgress.observe(this, saveStateObserver)

        binding.btSaveEvent.setOnClickListener {

            val setDataResult = eventsViewModel.setData(
                personNameInput = binding.etName.text.toString(),
                descriptionInput = binding.etDescription.text.toString(),
                typeInput = binding.spinner.selectedItemPosition
            )

            when (setDataResult) {
                EntityValidator.Result.SUCCESS -> eventsViewModel.insertEvent(eventsViewModel.eventEntityEditor.currentEventEntity)
                EntityValidator.Result.MISSING_DATE -> showMessage(getString(R.string.missing_date))
                EntityValidator.Result.MISSING_PERSON -> showMessage(getString(R.string.missing_person))
                EntityValidator.Result.MISSING_DESCRIPTION -> showMessage(getString(R.string.missing_desc))
                EntityValidator.Result.MISSING_ANY_INFO -> showMessage(getString(R.string.missing_any_info))
            }
        }

        binding.btDayPicker.setOnClickListener {
            val cal = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    //println("selected $dayOfMonth $monthOfYear $year")
                    eventsViewModel.setData(
                        dateInput = GregorianCalendar(year, monthOfYear, dayOfMonth).timeInMillis
                    )
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    private fun showMessage(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok, null)
        builder.show()
    }

    private val saveStateObserver: Observer<LiveDataEvent<OperationProgressState>> = Observer {
        it.getContentIfNotHandled()?.let { state ->
            when (state.status) {
                OperationProgressState.Status.FAILED ->
                    Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show()
                OperationProgressState.Status.SUCCESS -> {
                    Toast.makeText(this, R.string.save_success, Toast.LENGTH_LONG).show()
                    //finish()
                }
                OperationProgressState.Status.RUNNING -> {
                }
            }
        }
    }
}
