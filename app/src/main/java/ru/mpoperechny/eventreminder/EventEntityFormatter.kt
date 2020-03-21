package ru.mpoperechny.eventreminder

import ru.mpoperechny.eventreminder.database.EventEntity

class EventEntityFormatter {
    var currentEventEntity = EventEntity(EventEntity.Values.DATE_UNDEFINED_VALUE, 0, null, null)

    fun setData(
        dateInput: Long? = null,
        typeInput: Int? = null,
        personNameInput: String? = null,
        descriptionInput: String? = null
    ): EntityValidator.Result {
        dateInput?.let{ currentEventEntity.date = it}
        typeInput?.let {
            when(it) {
                0 -> currentEventEntity.type = EventEntity.BIRTHDAY
                1 -> currentEventEntity.type = EventEntity.HOLIDAY
                2 -> currentEventEntity.type = EventEntity.OTHER
            }
        }
        if(!personNameInput.isNullOrBlank()) currentEventEntity.person = personNameInput
        if(!descriptionInput.isNullOrBlank()) currentEventEntity.description = descriptionInput

        return EntityValidator.checkEntityData(currentEventEntity)
    }


}
