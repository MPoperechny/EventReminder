package ru.mpoperechny.eventreminder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mpoperechny.eventreminder.repository.EventsRepository

class EditEventViewModelFactory(private val repository: EventsRepository, private val eventId: Int? = null) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditEventViewModel(repository, eventId) as T
    }

}