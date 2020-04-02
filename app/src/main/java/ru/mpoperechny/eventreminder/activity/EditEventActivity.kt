package ru.mpoperechny.eventreminder.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import ru.mpoperechny.eventreminder.EntityValidator
import ru.mpoperechny.eventreminder.LiveDataEvent
import ru.mpoperechny.eventreminder.OperationProgressState
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.databinding.ActivityEditEventBinding
import ru.mpoperechny.eventreminder.utilites.FactoryProvider.provideViewModelFactory
import ru.mpoperechny.eventreminder.viewmodel.EditEventViewModel
import java.util.*


class EditEventActivity : AppCompatActivity() {

    companion object {
        const val EVENT_ID = "event_id"
    }

    private var eventId: Int? = null

    private val viewModel: EditEventViewModel by viewModels {
        provideViewModelFactory(application, eventId)
    }
    private val binding: ActivityEditEventBinding by lazy {
        DataBindingUtil.setContentView<ActivityEditEventBinding>(this, R.layout.activity_edit_event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let { if (it.hasExtra(EVENT_ID)) eventId = it.extras?.getInt(EVENT_ID) }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.newEvent.observe(this) {
            it?.let {
                if (it) supportActionBar?.title = getString(R.string.new_event_page_toolbar_title)
                else supportActionBar?.title = getString(R.string.edit_event_page_toolbar_title)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.saveProgress.observe(this, saveStateObserver)

        binding.btSaveEvent.setOnClickListener {
            sendData()

            when (viewModel.validateCurrentData()) {
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
                    sendData(GregorianCalendar(year, monthOfYear, dayOfMonth).timeInMillis)
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
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

    private fun sendData(time: Long? = null) {
        viewModel.setData(
            dateInput = time,
            typeInput = binding.spinner.selectedItemPosition,
            personNameInput = binding.etName.text.toString(),
            descriptionInput = binding.etDescription.text.toString()
        )
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
