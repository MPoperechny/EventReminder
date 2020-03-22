package ru.mpoperechny.eventreminder.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.mpoperechny.eventreminder.EntityValidator
import ru.mpoperechny.eventreminder.EventEntityEditor
import ru.mpoperechny.eventreminder.LiveDataEvent
import ru.mpoperechny.eventreminder.OperationProgressState
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.repository.EventsRepository

//todo change AndroidViewModel to ViewModel
class EventsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventsRepository

    internal var allEvents: LiveData<List<EventEntity>>

    init {
        repository = EventsRepository(application)
        allEvents = repository.allEvents

        println("EventsViewModel init ${this}")
    }

    val emptyData: LiveData<Boolean> = Transformations.map(allEvents) { it.isEmpty() }

    val sortedAllEvents: LiveData<List<EventEntity>> =
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
            val nearestEventDay = list[0].date
            if (includeDate) list.filter { it.date == nearestEventDay } else list.filter { it.date != nearestEventDay }
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


    // add/edit event page

    val eventEntityEditor = EventEntityEditor()

    private val _saveProgress = MutableLiveData<LiveDataEvent<OperationProgressState>>()
    val saveProgress: LiveData<LiveDataEvent<OperationProgressState>>
        get() = _saveProgress

    private val _currentEventDate = MutableLiveData<Long?>()
    val currentEventDate: LiveData<Long?>
        get() = _currentEventDate

    fun insertEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                _saveProgress.value = LiveDataEvent(OperationProgressState.STARTED)
                repository.insertEvent(event)
                _saveProgress.value = LiveDataEvent(OperationProgressState.READY)
            } catch (e: Exception) {
                _saveProgress.value = LiveDataEvent(OperationProgressState.error(e.message))
            }
        }
    }

    fun setData(
        dateInput: Long? = null,
        typeInput: Int? = null,
        personNameInput: String? = null,
        descriptionInput: String? = null
    ): EntityValidator.Result {
        val result = eventEntityEditor.setData(dateInput, typeInput, personNameInput, descriptionInput)

        var currentDate:Long? = eventEntityEditor.currentEventEntity.date;
        if(currentDate == EventEntity.Values.DATE_UNDEFINED_VALUE) currentDate = null
        _currentEventDate.postValue(currentDate)

        return result
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
