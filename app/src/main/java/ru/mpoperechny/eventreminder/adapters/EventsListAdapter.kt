package ru.mpoperechny.eventreminder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.mpoperechny.eventreminder.BR
import ru.mpoperechny.eventreminder.TextEventDescription
import ru.mpoperechny.eventreminder.database.EventEntity


class EventsListAdapter(private val listItemLayoutId: Int) : RecyclerView.Adapter<EventsListAdapter.ViewHolder>() {

    protected var mDataset = mutableListOf<EventEntity>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ViewDataBinding>(view)
    }

    override fun getItemViewType(position: Int): Int {
        return listItemLayoutId
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventsListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    fun updateList(newData: List<EventEntity>) {
        mDataset = newData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EventsListAdapter.ViewHolder, position: Int) {
        val eventEntity = mDataset[position]
        val textDescription = TextEventDescription(holder.itemView.context, eventEntity)
        holder.binding?.setVariable(BR.description, textDescription)
    }

}
