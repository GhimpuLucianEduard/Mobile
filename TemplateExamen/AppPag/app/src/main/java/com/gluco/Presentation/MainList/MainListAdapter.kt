package com.gluco.Presentation.MainList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gluco.Data.Local.NoteDomainModel
import com.gluco.ExamenApp
import com.gluco.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_list_card.view.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.gluco.Presentation.MainActivity


class MainListAdapter(val viewModel: MainListViewModel, val menuListener: CardClickListener) : RecyclerView.Adapter<MainListAdapter.MainListAdapterViewHolder>() {

    private var entries: List<NoteDomainModel> = ArrayList()

    fun setItems(data: List<NoteDomainModel>) {
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
        holder.completed.text = item.date
    }

    inner class MainListAdapterViewHolder(view: View, viewModel: MainListViewModel) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.titleTextView
        val completed: TextView = view.completedTextView

        init {
            view.deleteItem.setOnClickListener {
                menuListener.onDeletedClicked(entries[layoutPosition])
            }
//            view. .setOnClickListener {
//                val popup = PopupMenu(view.context, view.menuButton)
//                val inflater = popup.menuInflater
//                inflater.inflate(R.menu.card_popup_menu, popup.menu)
//                popup.setOnMenuItemClickListener { it ->
//                    when {
//                        it.itemId == R.id.deleteItem -> menuListener.onDeleteClicked(entries[layoutPosition])
//
//                        it.itemId == R.id.editItem -> menuListener.onEditClicked(entries[layoutPosition])
//                    }
//                    true
//                }
//                popup.show()
//            }
        }
    }
}

