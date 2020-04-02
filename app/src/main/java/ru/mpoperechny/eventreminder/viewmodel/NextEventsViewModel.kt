package ru.mpoperechny.eventreminder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.repository.EventsRepository
import java.util.*

class NextEventsViewModel(repository: EventsRepository) : BaseEventViewModel(repository) {

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

}
