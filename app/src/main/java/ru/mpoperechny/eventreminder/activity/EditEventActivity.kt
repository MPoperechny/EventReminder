package ru.mpoperechny.eventreminder.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import ru.mpoperechny.eventreminder.*
import ru.mpoperechny.eventreminder.databinding.ActivityEditEventBinding
import ru.mpoperechny.eventreminder.utilites.FactoryProvider.provideEditEventViewModelFactory
import ru.mpoperechny.eventreminder.utilites.timeToDateString
import ru.mpoperechny.eventreminder.viewmodel.EditEventViewModel
import java.util.*


class EditEventActivity : AppCompatActivity() {

    //todo горизонтальная ориентация

    companion object {
        const val EVENT_ID = "event_id"
    }

    private var eventId: Int? = null

    private val viewModel: EditEventViewModel by viewModels {
        provideEditEventViewModelFactory(application, eventId)
    }
    private val binding: ActivityEditEventBinding by lazy {
        DataBindingUtil.setContentView<ActivityEditEventBinding>(this, R.layout.activity_edit_event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let { if (it.hasExtra(EVENT_ID)) eventId = it.extras?.getInt(EVENT_ID) }

        supportActionBar?.title = getString(R.string.new_event_page_toolbar_title)

        //binding.viewModel = eventsViewModel

        viewModel.saveProgress.observe(this, saveStateObserver)
        viewModel.currentEventDate.observe(this) {
            binding.tvDayPicker.text =
                if (it != null) timeToDateString(it) else getString(R.string.select_date)
        }

        binding.btSaveEvent.setOnClickListener {

            val setDataResult = viewModel.setData(
                personNameInput = binding.etName.text.toString(),
                descriptionInput = binding.etDescription.text.toString(),
                typeInput = binding.spinner.selectedItemPosition
            )

            when (setDataResult) {
                EntityValidator.Result.SUCCESS -> viewModel.saveEvent()
                EntityValidator.Result.MISSING_DATE -> showMessage(getString(R.string.missing_date))
                EntityValidator.Result.MISSING_PERSON -> showMessage(getString(R.string.missing_person))
                EntityValidator.Result.MISSING_DESCRIPTION -> showMessage(getString(R.string.missing_desc))
                EntityValidator.Result.MISSING_ANY_INFO -> showMessage(getString(R.string.missing_any_info))
            }
        }

        binding.tvDayPicker.setOnClickListener {
            val cal = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    //println("selected $dayOfMonth $monthOfYear $year")
                    val time = GregorianCalendar(year, monthOfYear, dayOfMonth).timeInMillis
                    viewModel.setData(dateInput = time)
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    private fun showMessage(message: String, onClick: DialogInterface.OnClickListener? = null) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok, onClick)
        builder.show()
    }

    private val saveStateObserver: Observer<LiveDataEvent<OperationProgressState>> = Observer {
        it.getContentIfNotHandled()?.let { state ->
            when (state.status) {
                OperationProgressState.Status.FAILED ->
                    showMessage(getString(R.string.error))
                OperationProgressState.Status.SUCCESS -> {
                    showMessage(
                        getString(R.string.save_success),
                        DialogInterface.OnClickListener { _, _ -> finish() })
                }
                OperationProgressState.Status.RUNNING -> {
                }
            }
        }
    }
}
