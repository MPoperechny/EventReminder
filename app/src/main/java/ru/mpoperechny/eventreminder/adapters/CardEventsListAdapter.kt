package ru.mpoperechny.eventreminder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


import ru.mpoperechny.eventreminder.R
import kotlin.collections.ArrayList


class CardEventsListAdapter(private val mDataset: ArrayList<List<String?>>) :
    RecyclerView.Adapter<CardEventsListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return mDataset.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvTitle: TextView = v.findViewById(R.id.tv_title) as TextView
        var tvType: TextView = v.findViewById(R.id.tv_type) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardEventsListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_events_list_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvTitle.text = mDataset[position][0]
        holder.tvType.text = mDataset[position][1]

    }

}
