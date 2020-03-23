package ru.mpoperechny.eventreminder.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.mpoperechny.eventreminder.EntityValidator
import ru.mpoperechny.eventreminder.EventEntityEditor
import ru.mpoperechny.eventreminder.LiveDataEvent
import ru.mpoperechny.eventreminder.OperationProgressState
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.repository.EventsRepository

class EditEventViewModel(
    private val repository: EventsRepository,
    private val eventId: Int? = null
) : ViewModel() {

    var currentEventEntity = EventEntity(EventEntity.Values.DATE_UNDEFINED_VALUE, 0, null, null)

    private var allEvents: LiveData<List<EventEntity>> = repository.allEvents

    private val eventEntityEditor = EventEntityEditor()

    private var newEventMode = true

    private val _currentEventDate = MutableLiveData<Long?>()
    val currentEventDate: LiveData<Long?>
        get() = _currentEventDate

    private val _currentEventType = MutableLiveData<Int?>()
    val currentEventType: LiveData<Int?>
        get() = _currentEventType

    private val _currentEventPerson = MutableLiveData<String?>()
    val currentEventPerson: LiveData<String?>
        get() = _currentEventPerson

    private val _currentEventDesc = MutableLiveData<String?>()
    val currentEventDesc: LiveData<String?>
        get() = _currentEventDesc

    private val _newEvent = MutableLiveData<Boolean>()
    val newEvent: LiveData<Boolean?>
        get() = _newEvent

    init {
        eventId?.let {
            val currentEvent = allEvents.value?.singleOrNull { eventEntity -> eventEntity.id == it }
            currentEvent?.let {
                currentEventEntity = currentEvent
                newEventMode = false
                updateViewData()
            }
        }

        _newEvent.value = newEventMode
    }

    private val _saveProgress = MutableLiveData<LiveDataEvent<OperationProgressState>>()
    val saveProgress: LiveData<LiveDataEvent<OperationProgressState>>
        get() = _saveProgress


    //todo use update/replace for edit mode
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
    ) {
        eventEntityEditor.setData(currentEventEntity, dateInput, typeInput, personNameInput, descriptionInput)
        updateViewData()
    }

    fun validateCurrentData(): EntityValidator.Result {
        return EntityValidator.checkEntityData(currentEventEntity)
    }

    private fun updateViewData() {
        var currentDate: Long? = currentEventEntity.date;
        if (currentDate == EventEntity.Values.DATE_UNDEFINED_VALUE) currentDate = null
        _currentEventDate.value = currentDate

        _currentEventPerson.value = currentEventEntity.person
        _currentEventDesc.value = currentEventEntity.description
        _currentEventType.value = currentEventEntity.type
    }

    fun saveEvent() {
        insertEvent(currentEventEntity)
    }

}
