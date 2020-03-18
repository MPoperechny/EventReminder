package ru.mpoperechny.eventreminder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.database.EventEntity


private var mDataset = mutableListOf<EventEntity>()

class AllEventsListAdapter : RecyclerView.Adapter<AllEventsListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return mDataset.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTextView: TextView

        init {
            mTextView = v.findViewById(R.id.tv_recycler_item) as TextView
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllEventsListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.next_events_list_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.text = mDataset[position].person
    }

    fun updateList(newData: List<EventEntity>) {
        mDataset = newData.toMutableList()
        notifyDataSetChanged()
    }


}
