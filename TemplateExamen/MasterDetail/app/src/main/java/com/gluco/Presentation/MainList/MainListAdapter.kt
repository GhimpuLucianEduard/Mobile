package com.gluco.Presentation.MainList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gluco.Data.Local.TaskDomainModel
import com.gluco.R
import kotlinx.android.synthetic.main.main_list_card.view.*


class MainListAdapter(val viewModel: MainListViewModel, val menuListener: OnMenuCardItemClickedListener) : RecyclerView.Adapter<MainListAdapter.MainListAdapterViewHolder>() {

    private var entries: List<TaskDomainModel> = ArrayList()

    fun setItems(data: List<TaskDomainModel>) {
        entries = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListAdapterViewHolder {
        return MainListAdapterViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.main_list_card, parent, false), viewModel)
    }

    override fun onBindViewHolder(holder: MainListAdapterViewHolder, position: Int) {
        val item = entries[position]
        holder.title.text = item.text.toString()
        holder.completed.text = item.updated.toString()
    }

    inner class MainListAdapterViewHolder(view: View, viewModel: MainListViewModel) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.titleTextView
        val completed: TextView = view.completedTextView

        init {
            view.setOnClickListener {
                menuListener.onEditClicked(entries[layoutPosition])
            }
        }
    }
}

