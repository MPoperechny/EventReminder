package ru.mpoperechny.eventreminder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mpoperechny.eventreminder.repository.EventsRepository

class ViewModelFactory(
    private val repository: EventsRepository,
    private val eventId: Int? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        println("Creating ${modelClass.canonicalName}")

        return when {
            modelClass.isAssignableFrom(NextEventsViewModel::class.java) ->
                NextEventsViewModel(repository) as T
            modelClass.isAssignableFrom(AllEventsViewModel::class.java) ->
                AllEventsViewModel(repository) as T
            modelClass.isAssignableFrom(EditEventViewModel::class.java) ->
                EditEventViewModel(repository, eventId) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }

    }

}