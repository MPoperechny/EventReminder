package ru.mpoperechny.eventreminder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.repository.EventsRepository

open class BaseEventViewModel(protected val repository: EventsRepository) : ViewModel() {

    protected var allEvents: LiveData<List<EventEntity>> = repository.allEvents

    val emptyData: LiveData<Boolean> = Transformations.map(allEvents) { it.isEmpty() }
}