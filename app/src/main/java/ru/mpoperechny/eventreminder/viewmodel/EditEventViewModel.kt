package ru.mpoperechny.eventreminder.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.mpoperechny.eventreminder.EntityValidator
import ru.mpoperechny.eventreminder.EventEntityEditor
import ru.mpoperechny.eventreminder.LiveDataEvent
import ru.mpoperechny.eventreminder.OperationProgressState
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.repository.EventsRepository

class EditEventViewModel(private val repository: EventsRepository, private val eventId: Int? = null) :
    ViewModel() {

    init{
        println("set eventId $eventId")
    }

    private var allEvents: LiveData<List<EventEntity>> = repository.allEvents

    //val emptyData: LiveData<Boolean> = Transformations.map(allEvents) { it.isEmpty() }

    private val eventEntityEditor = EventEntityEditor()

    private val _saveProgress = MutableLiveData<LiveDataEvent<OperationProgressState>>()
    val saveProgress: LiveData<LiveDataEvent<OperationProgressState>>
        get() = _saveProgress

    private val _currentEventDate = MutableLiveData<Long?>()
    val currentEventDate: LiveData<Long?>
        get() = _currentEventDate

    private fun insertEvent(event: EventEntity) {
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
        val result =
            eventEntityEditor.setData(dateInput, typeInput, personNameInput, descriptionInput)

        var currentDate: Long? = eventEntityEditor.currentEventEntity.date;
        if (currentDate == EventEntity.Values.DATE_UNDEFINED_VALUE) currentDate = null
        _currentEventDate.postValue(currentDate)

        return result
    }

    fun saveEvent() {
        insertEvent(eventEntityEditor.currentEventEntity)
    }


}
