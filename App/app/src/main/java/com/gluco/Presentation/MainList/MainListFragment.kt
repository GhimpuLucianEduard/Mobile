package com.gluco.Presentation.MainList

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Presentation.ScopedFragment
import com.gluco.R
import kotlinx.android.synthetic.main.main_list_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MainListFragment : ScopedFragment(), KodeinAware, PopupMenu.OnMenuItemClickListener, OnMenuCardItemClickedListener {

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        Toast.makeText(activity, "click", Toast.LENGTH_SHORT).show()
        return false
    }

    override val kodein by closestKodein()
    private val viewModelFactory: MainListViewModelFactory by instance()
    private lateinit var viewModel: MainListViewModel
    private lateinit var adapter: MainListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.main_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainListViewModel::class.java)
        bindUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.main_list_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            R.id.deleteIcon -> {
                Toast.makeText(activity, "Delete", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindUI() = launch {
        // init list view
        initRecycleView()
    }

    private fun initRecycleView() = launch {
        adapter = MainListAdapter(viewModel, this@MainListFragment)
        val entries = viewModel.entries.await()
        entries.observe(this@MainListFragment, Observer {
            adapter.setItems(it)
            progressBar.visibility = View.GONE
        })
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    override fun onEditClicked(glucoseEntry: GlucoseEntry) {
        val action = MainListFragmentDirections.editEntryAction()
        Navigation.findNavController(activity!!, R.id.nav_host_fragment).navigate(action)
    }

}
