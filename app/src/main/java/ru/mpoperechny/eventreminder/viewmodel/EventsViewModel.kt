package ru.mpoperechny.eventreminder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.mpoperechny.eventreminder.database.EventEntity

import ru.mpoperechny.eventreminder.repository.EventsRepository

class EventsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventsRepository

    internal var allEvents: LiveData<List<EventEntity>>?

    init {
        repository = EventsRepository(application)
        allEvents = repository.allEvents
    }

    fun insertEvent(event: EventEntity) {
        repository.insertEvent(event)
    }

    fun insertEvents(vararg events: EventEntity) {
        repository.insertEvents(*events)
    }

    fun deleteEvent(event: EventEntity) {
        repository.deleteEvent(event)
    }
}
