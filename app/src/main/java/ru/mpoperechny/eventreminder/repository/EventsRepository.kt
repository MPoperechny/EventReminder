package ru.mpoperechny.eventreminder.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.database.EventsDAO
import ru.mpoperechny.eventreminder.database.EventsDatabase

class EventsRepository private constructor(application: Application) {

    private var eventsDAO: EventsDAO = EventsDatabase.getDatabase(application).eventsDao
    var allEvents: LiveData<List<EventEntity>>

    init {
        allEvents = eventsDAO.allEvents
        println("EventsRepository init ${this}")
    }


    suspend fun insertEvent(event: EventEntity) {
        eventsDAO.insertEvent(event)
    }

    suspend fun insertEvents(vararg events: EventEntity) {
        eventsDAO.insertEvents(*events)
    }

    suspend fun deleteEvent(event: EventEntity) {
        eventsDAO.deleteEvents(event)
    }

    suspend fun deleteAll() {
        eventsDAO.deleteAll()
    }

    companion object {
        private var instance: EventsRepository? = null

        fun getInstance(application: Application) =
            instance ?: EventsRepository(application).also { instance = it }
    }

}
