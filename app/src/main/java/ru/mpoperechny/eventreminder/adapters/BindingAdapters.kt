package ru.mpoperechny.eventreminder.adapters

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mpoperechny.eventreminder.database.EventEntity

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<EventEntity>?) {
    items?.let{(listView.adapter as EventsListAdapter).updateList(it)}
}

@BindingAdapter("app:activeItem")
fun bindSpinnerValue(spinner: Spinner, position :Int){
    spinner.setSelection(position);
}