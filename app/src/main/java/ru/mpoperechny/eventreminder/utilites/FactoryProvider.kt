package ru.mpoperechny.eventreminder.utilites

import android.app.Application
import android.content.Context
import ru.mpoperechny.eventreminder.repository.EventsRepository
import ru.mpoperechny.eventreminder.viewmodel.EventsViewModelFactory

object FactoryProvider {

    private fun getEventsRepository(application: Application): EventsRepository {
        return EventsRepository.getInstance(application)
    }

    fun provideEventsViewModelFactory(application: Application): EventsViewModelFactory {
        val repository = getEventsRepository(application)
        return EventsViewModelFactory(repository)
    }

}