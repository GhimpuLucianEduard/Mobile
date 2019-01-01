package com.gluco.Presentation.MainList

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.R
import com.gluco.Utility.timeStampToDateString
import kotlinx.android.synthetic.main.main_list_card.view.*
import kotlinx.coroutines.*

class MainListAdapter(val viewModel: MainListViewModel, val menuListener: OnMenuCardItemClickedListener) : RecyclerView.Adapter<MainListAdapter.MainListAdapterViewHolder>() {

    private var entries: List<GlucoseEntry> = ArrayList()

    fun setItems(data: List<GlucoseEntry>) {
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
        holder.value.text = item.value.toString() + " mg/dl"
        holder.afterMeal.text = if (item.afterMeal) "After Meal" else "Before Meal"
        holder.date.text = if (item.timestamp == null) "Undefined" else String.timeStampToDateString(item.timestamp)
        if (!item.afterMeal) {
            when {
                item.value in 40..59 -> holder.marginBox.setBackgroundColor(Color.parseColor("#008000"))
                item.value in 60..79 -> holder.marginBox.setBackgroundColor(Color.parseColor("#FFFF00"))
                item.value in 80..100 -> holder.marginBox.setBackgroundColor(Color.parseColor("#FFA500"))
                else -> holder.marginBox.setBackgroundColor(Color.parseColor("#FF0000"))
            }
        } else {
            when {
                item.value in 72..109 -> holder.marginBox.setBackgroundColor(Color.parseColor("#008000"))
                item.value in 110..144 -> holder.marginBox.setBackgroundColor(Color.parseColor("#FFFF00"))
                item.value in 145..180 -> holder.marginBox.setBackgroundColor(Color.parseColor("#FFA500"))
                else -> holder.marginBox.setBackgroundColor(Color.parseColor("#FF0000"))
            }
        }
    }

    inner class MainListAdapterViewHolder(view: View, viewModel: MainListViewModel) : RecyclerView.ViewHolder(view) {

        val value: TextView = view.valueTextView
        val date: TextView = view.dateTextView
        val afterMeal: TextView = view.afterMealTextView
        val marginBox: View = view.marginBoxView

        init {
            view.menuButton.setOnClickListener {
                val popup = PopupMenu(view.context, view.menuButton)
                val inflater = popup.menuInflater
                inflater.inflate(R.menu.card_popup_menu, popup.menu)
                popup.setOnMenuItemClickListener { it ->
                    when {
                        it.itemId == R.id.deleteItem -> GlobalScope.launch {
                            viewModel.deleteEntry(entries[layoutPosition].id!!)
                        }
                        it.itemId == R.id.editItem -> menuListener.onEditClicked(entries[layoutPosition])
                    }
                    true
                }
                popup.show()
            }
        }
    }
}

