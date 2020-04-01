package ru.mpoperechny.eventreminder.database

import java.util.*

class DefaultData {

    companion object {
        fun getDefaultData(): Array<EventEntity> {
            return arrayOf(
                EventEntity(
                    GregorianCalendar(1965, Calendar.FEBRUARY, 18).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Иванов Иван Иванович",
                    null
                ),
                EventEntity(
                    GregorianCalendar(1965, Calendar.FEBRUARY, 19).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Иванов Иван Петрович",
                    null
                ),
                EventEntity(
                    GregorianCalendar(1965, Calendar.FEBRUARY, 20).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Иванов Иван Сидорович",
                    null
                ),
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    null
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 2).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Иванович",
                    null
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 3).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Сидорович",
                    null
                )
            )
        }
    }

}