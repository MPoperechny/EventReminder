package ru.mpoperechny.eventreminder.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.adapters.EventsListAdapter
import ru.mpoperechny.eventreminder.databinding.ActivityAllEventsBinding
import ru.mpoperechny.eventreminder.utilites.EventImportExport
import ru.mpoperechny.eventreminder.utilites.FactoryProvider.provideViewModelFactory
import ru.mpoperechny.eventreminder.utilites.timeToDateString
import ru.mpoperechny.eventreminder.viewmodel.AllEventsViewModel


class AllEventsActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_EXPORT_FOLDER = 1001
        private const val REQUEST_CODE_IMPORT_FOLDER = 1002
    }

    private val viewModel: AllEventsViewModel by viewModels {
        provideViewModelFactory(application)
    }

    private val binding: ActivityAllEventsBinding by lazy {
        DataBindingUtil.setContentView<ActivityAllEventsBinding>(this, R.layout.activity_all_events)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.all_events_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_export_events -> {
                exportEvents()
                return true
            }
            R.id.action_import_events -> {
                importEvents()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exportEvents() {
        val events = viewModel.allEventsSortedByDayOfYear.value ?: emptyList()
        if (events.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_data_text), Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, REQUEST_CODE_EXPORT_FOLDER)
    }

    private fun importEvents() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, REQUEST_CODE_IMPORT_FOLDER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || data == null || data.data == null) return

        val uri = data.data ?: return
        contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )

        when (requestCode) {
            REQUEST_CODE_EXPORT_FOLDER -> exportEventsToFolder(uri)
            REQUEST_CODE_IMPORT_FOLDER -> importEventsFromFolder(uri)
        }
    }

    private fun exportEventsToFolder(folderUri: Uri) {
        val events = viewModel.allEventsSortedByDayOfYear.value ?: emptyList()
        val folder = DocumentFile.fromTreeUri(this, folderUri) ?: run {
            Toast.makeText(this, getString(R.string.export_error), Toast.LENGTH_SHORT).show()
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val payload = EventImportExport.toJson(events)
                val targetFile = folder.findFile(EventImportExport.FILE_NAME)
                    ?: folder.createFile("application/json", EventImportExport.FILE_NAME)
                    ?: throw IllegalStateException("Unable to create file")

                contentResolver.openOutputStream(targetFile.uri)?.use { outputStream ->
                    outputStream.write(payload.toByteArray())
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AllEventsActivity,
                        getString(R.string.export_success, targetFile.name ?: EventImportExport.FILE_NAME),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AllEventsActivity, getString(R.string.export_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun importEventsFromFolder(folderUri: Uri) {
        val folder = DocumentFile.fromTreeUri(this, folderUri) ?: run {
            Toast.makeText(this, getString(R.string.import_error), Toast.LENGTH_SHORT).show()
            return
        }

        val files = folder.listFiles().filter { it.isFile && (it.name ?: "").toLowerCase().endsWith(".json") }
        when {
            files.isEmpty() -> {
                Toast.makeText(this, getString(R.string.import_file_missing), Toast.LENGTH_SHORT).show()
            }
            files.size == 1 -> importEventsFromDocumentFile(files[0])
            else -> {
                val fileNames = files.map { it.name ?: EventImportExport.FILE_NAME }.toTypedArray()
                AlertDialog.Builder(this)
                    .setTitle(R.string.import_events)
                    .setItems(fileNames) { _, which -> importEventsFromDocumentFile(files[which]) }
                    .show()
            }
        }
    }

    private fun importEventsFromDocumentFile(file: DocumentFile) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val input = contentResolver.openInputStream(file.uri) ?: throw IllegalStateException("Input stream is null")
                val importedEvents = input.bufferedReader().use { reader ->
                    EventImportExport.fromJson(reader.readText())
                }

                withContext(Dispatchers.Main) {
                    viewModel.replaceAllEvents(importedEvents)
                    Toast.makeText(
                        this@AllEventsActivity,
                        getString(R.string.import_success, importedEvents.size),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AllEventsActivity, getString(R.string.import_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
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
