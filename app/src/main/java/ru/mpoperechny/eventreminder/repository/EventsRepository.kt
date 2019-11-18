package ru.mpoperechny.eventreminder.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.database.EventsDAO
import ru.mpoperechny.eventreminder.database.EventsDatabase

class EventsRepository internal constructor(application: Application) {

    private var eventsDAO: EventsDAO = EventsDatabase.getDatabase(application).eventsDao
    var allEvents: LiveData<List<EventEntity>>

    init {
        allEvents = eventsDAO.allEvents
    }


    fun insertEvent(event: EventEntity) {
        AsyncTask.execute { eventsDAO.insertEvent(event) }
    }

    fun insertEvents(vararg events: EventEntity) {
        AsyncTask.execute { eventsDAO.insertEvents(*events) }
    }

    fun deleteEvent(event: EventEntity) {
        AsyncTask.execute { eventsDAO.deleteEvents(event) }
    }

}
