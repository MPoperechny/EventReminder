package ru.mpoperechny.eventreminder
import ru.mpoperechny.eventreminder.database.EventEntity

class EntityValidator {
    companion object {
        fun checkEntityData(eventEntity: EventEntity): Result {
            var result = Result.SUCCESS
            with(eventEntity) {
                if (date == 0L) result = Result.MISSING_DATE
                when (type) {
                    EventEntity.BIRTHDAY -> if (person == null) result = Result.MISSING_PERSON
                    EventEntity.HOLIDAY -> if (description == null) result =
                        Result.MISSING_DESCRIPTION
                    EventEntity.OTHER -> if (person == null && description == null) result =
                        Result.MISSING_ANY_INFO
                }
            }
            return result
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