package ru.mpoperechny.eventreminder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.getBinding
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.database.EventEntity
import ru.mpoperechny.eventreminder.databinding.AllEventsListItemBinding

abstract class BaseEventsListAdapter : RecyclerView.Adapter<BaseEventsListAdapter.ViewHolder>() {

    protected var mDataset = mutableListOf<EventEntity>()

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ViewDataBinding>(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseEventsListAdapter.ViewHolder {
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

}
