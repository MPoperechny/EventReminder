package ru.mpoperechny.eventreminder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mpoperechny.eventreminder.TextEventDescription
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.databinding.AllEventsListItemBinding

private var mDataset = mutableListOf<EventEntity>()

class AllEventsListAdapter : RecyclerView.Adapter<AllEventsListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return mDataset.size
    }

    class ViewHolder(val binding: AllEventsListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AllEventsListItemBinding.inflate(layoutInflater)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventEntity = mDataset[position]

        val textDescription = TextEventDescription(holder.binding.root.context, eventEntity)

        with(holder.binding){
            descriptionText = textDescription.fullDescriptionText
            executePendingBindings()
        }
    }

    fun updateList(newData: List<EventEntity>) {
        mDataset = newData.toMutableList()
        notifyDataSetChanged()
    }
}
