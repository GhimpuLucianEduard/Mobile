package com.gluco.Presentation.MainList

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Presentation.MainActivity
import com.gluco.Presentation.ScopedFragment
import com.gluco.R
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_list_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import androidx.recyclerview.widget.RecyclerView



class MainListFragment : ScopedFragment(), KodeinAware, OnMenuCardItemClickedListener {

    override val kodein by closestKodein()
    private val viewModelFactory: MainListViewModelFactory by instance()
    private lateinit var viewModel: MainListViewModel
    private lateinit var adapter: MainListAdapter
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var scrollListener: EndlessScrollListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.main_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainListViewModel::class.java)
        bindUI()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun bindUI() = launch {
        // init list view
//        addButton.setOnClickListener {
//            viewModel.selectedEntry.value = GlucoseEntry()
////            val action = MainListFragmentDirections.editEntryAction()
////            Navigation.findNavController(activity!!, R.id.nav_host_fragment)
////                .navigate(action)
//        }
        initRecycleView()
    }

    private fun initRecycleView() = launch {
        adapter = MainListAdapter(viewModel, this@MainListFragment)
        val entries = viewModel.entries
        entries.observe(this@MainListFragment, Observer {
            adapter.setItems(it)
            progressBar.visibility = View.GONE
        })
        val linearLayout = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayout
        recyclerView.adapter = adapter
        scrollListener = object : EndlessScrollListener(linearLayout) {
            override fun onLoadMore(page: Int) {
                progressBar.visibility = View.VISIBLE
                viewModel.getEntriesByPage(page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            this.loading = false
                            progressBar.visibility = View.GONE
                        }, {
                            Log.e("Error", "Fetch data failed: ${it.message}")
                            progressBar.visibility = View.GONE
                        }, {
                            Log.e("Error", "Fetched data finished")
                            progressBar.visibility = View.GONE
                        })
                        .addTo(compositeDisposable)
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun onDeleteClicked(entry: GlucoseEntry) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditClicked(glucoseEntry: GlucoseEntry) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
