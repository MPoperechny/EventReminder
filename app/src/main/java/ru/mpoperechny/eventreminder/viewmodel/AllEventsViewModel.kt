package ru.mpoperechny.eventreminder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.repository.EventsRepository
import java.util.*

class AllEventsViewModel(repository: EventsRepository) : BaseEventViewModel(repository) {

    val allEventsSortedByDayOfYear: LiveData<List<EventEntity>> =
        Transformations.map(allEvents) { eventEntities ->
            val calendar = Calendar.getInstance()
            eventEntities.sortedBy {
                calendar.timeInMillis = it.date
                calendar.get(Calendar.DAY_OF_YEAR)
            }
        }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch { repository.deleteEvent(event) }
    }

    fun deleteAll() {
        viewModelScope.launch { repository.deleteAll() }
    }
}
