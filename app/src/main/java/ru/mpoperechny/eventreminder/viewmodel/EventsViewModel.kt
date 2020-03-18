package ru.mpoperechny.eventreminder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mpoperechny.eventreminder.database.EventEntity

import ru.mpoperechny.eventreminder.repository.EventsRepository

class EventsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventsRepository

    internal var allEvents: LiveData<List<EventEntity>>

    init {
        repository = EventsRepository(application)
        allEvents = repository.allEvents

        println("EventsViewModel init ${this}")
    }

    fun insertEvent(event: EventEntity) {
        viewModelScope.launch { repository.insertEvent(event) }
    }

    fun insertEvents(vararg events: EventEntity) {
        viewModelScope.launch { repository.insertEvents(*events) }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch { repository.deleteEvent(event) }
    }

    fun deleteAll() {
        viewModelScope.launch { repository.deleteAll() }
    }
}
