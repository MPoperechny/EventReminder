package ru.mpoperechny.eventreminder

import ru.mpoperechny.eventreminder.database.EventEntity

class EventEntityFormatter {
    var currentEventEntity = EventEntity(0, 0, null, null)

    fun setData(
        date: Long = 0,
        type: Int = 0,
        person: String? = null,
        description: String? = null
    ): EntityValidator.Result {
        if (date != 0L) currentEventEntity.date = date
        if (type != 0) currentEventEntity.type = type
        if(!person.isNullOrBlank()) currentEventEntity.person = person
        if(!description.isNullOrBlank()) currentEventEntity.description = description

        return EntityValidator.checkEntityData(currentEventEntity)
    }


}
