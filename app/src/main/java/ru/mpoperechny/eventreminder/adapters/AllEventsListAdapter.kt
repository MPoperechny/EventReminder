package ru.mpoperechny.eventreminder.adapters

import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.TextEventDescription
import ru.mpoperechny.eventreminder.databinding.AllEventsListItemBinding

class AllEventsListAdapter : BaseEventsListAdapter() {

    override fun getItemViewType(position: Int): Int {
        return R.layout.all_events_list_item
    }

    override fun onBindViewHolder(holder: BaseEventsListAdapter.ViewHolder, position: Int) {

        val eventEntity = mDataset[position]

        val textDescription = TextEventDescription(holder.itemView.context, eventEntity)

        with(holder.binding as AllEventsListItemBinding){
            descriptionText = textDescription.fullDescriptionText
            executePendingBindings()
        }
    }
}
