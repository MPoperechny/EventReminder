package ru.mpoperechny.eventreminder.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mpoperechny.eventreminder.database.EventEntity

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<EventEntity>?) {
    items?.let{(listView.adapter as EventsListAdapter).updateList(it)}
}