package ru.mpoperechny.eventreminder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


import java.util.ArrayList

import ru.mpoperechny.eventreminder.R


class NextEventsListAdapter(private val mDataset: ArrayList<String?>) :
    RecyclerView.Adapter<NextEventsListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return mDataset.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTextView: TextView

        init {
            mTextView = v.findViewById(R.id.tv_recycler_item) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextEventsListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.next_events_list_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mTextView.text = mDataset[position]

    }

}
