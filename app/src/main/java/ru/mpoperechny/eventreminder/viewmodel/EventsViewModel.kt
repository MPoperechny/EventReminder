package ru.mpoperechny.eventreminder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.repository.EventsRepository

class EventsViewModel(private val repository: EventsRepository) : ViewModel() {

    internal var allEvents: LiveData<List<EventEntity>> = repository.allEvents

    val emptyData: LiveData<Boolean> = Transformations.map(allEvents) { it.isEmpty() }

    private val sortedAllEvents: LiveData<List<EventEntity>> =
        Transformations.map(allEvents) { eventEntities ->
            eventEntities.sortedBy { it.daysLeft }
        }

    val nearestEvents: LiveData<List<EventEntity>> =
        Transformations.map(sortedAllEvents) { sortedEventEntities ->
            filterList(sortedEventEntities, true)
        }

    val eventsExceptNearest: LiveData<List<EventEntity>> =
        Transformations.map(sortedAllEvents) { sortedEventEntities ->
            filterList(sortedEventEntities, false)
        }

    private fun filterList(list: List<EventEntity>, includeDate: Boolean): List<EventEntity> {
        return if (list.isNotEmpty()) {
            val minDaysLeft = list[0].daysLeft
            if (includeDate) list.filter { it.daysLeft == minDaysLeft } else list.filter { it.daysLeft != minDaysLeft }
        } else {
            list
        }
    }

    val nearestEventDay: LiveData<Long> = Transformations.map(sortedAllEvents) {
        if (it.isNotEmpty()) it[0].date else 0
    }

    val nearestEventDaysLeft: LiveData<Int> = Transformations.map(sortedAllEvents) {
        if (it.isNotEmpty()) it[0].daysLeft else 0
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
