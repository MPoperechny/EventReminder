package ru.mpoperechny.eventreminder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mpoperechny.eventreminder.repository.EventsRepository

class EventsViewModelFactory(private val repository: EventsRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventsViewModel(repository) as T
    }

}