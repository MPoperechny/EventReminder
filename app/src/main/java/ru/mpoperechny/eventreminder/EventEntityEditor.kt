package ru.mpoperechny.eventreminder

import ru.mpoperechny.eventreminder.database.EventEntity

class EventEntityEditor {
    fun setData(
        currentEventEntity: EventEntity,
        dateInput: Long? = null,
        typeInput: Int? = null,
        personNameInput: String? = null,
        descriptionInput: String? = null
    ) {
        dateInput?.let { currentEventEntity.date = it }
        typeInput?.let {
            when (it) {
                0 -> currentEventEntity.type = EventEntity.BIRTHDAY
                1 -> currentEventEntity.type = EventEntity.HOLIDAY
                2 -> currentEventEntity.type = EventEntity.OTHER
            }
        }
        if (!personNameInput.isNullOrBlank()) currentEventEntity.person = personNameInput
        if (!descriptionInput.isNullOrBlank()) currentEventEntity.description = descriptionInput
    }

}
