package ru.mpoperechny.eventreminder

import ru.mpoperechny.eventreminder.database.EventEntity

class EntityValidator {
    companion object {
        fun checkEntityData(eventEntity: EventEntity): Result {
            with(eventEntity) {
                if (date == EventEntity.Values.DATE_UNDEFINED_VALUE) return Result.MISSING_DATE
                when (type) {
                    EventEntity.BIRTHDAY -> if (person == null) return Result.MISSING_PERSON
                    EventEntity.HOLIDAY -> if (description == null) return Result.MISSING_DESCRIPTION
                    EventEntity.OTHER -> if (person == null && description == null) return Result.MISSING_ANY_INFO
                }
            }
            return Result.SUCCESS
        }
    }

    enum class Result {
        SUCCESS,
        MISSING_DATE,
        MISSING_PERSON,
        MISSING_DESCRIPTION,
        MISSING_ANY_INFO
    }
}