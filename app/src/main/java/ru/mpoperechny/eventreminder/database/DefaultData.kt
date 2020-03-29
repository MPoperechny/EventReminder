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
                    "описание1"
                ),
                EventEntity(
                    GregorianCalendar(1965, Calendar.FEBRUARY, 18).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Иванов Иван Иванович",
                    "описание1"
                ),
                EventEntity(
                    GregorianCalendar(1965, Calendar.FEBRUARY, 18).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Иванов Иван Иванович",
                    "описание1"
                ),
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )
            )
        }
    }

}