package ru.mpoperechny.eventreminder.utilites

import android.app.Application
import ru.mpoperechny.eventreminder.repository.EventsRepository
import ru.mpoperechny.eventreminder.viewmodel.ViewModelFactory

object FactoryProvider {

    fun getEventsRepository(application: Application): EventsRepository {
        return EventsRepository.getInstance(application)
    }

    fun provideViewModelFactory(application: Application, eventId: Int? = null): ViewModelFactory {
        val repository = getEventsRepository(application)
        return ViewModelFactory(repository, eventId)
    }

}