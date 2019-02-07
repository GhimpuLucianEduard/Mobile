package com.gluco.Presentation.MainList

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.beust.klaxon.Json
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.lookup
import com.gluco.Data.Local.NoteDomainModel
import com.gluco.ExamenApp
import com.gluco.Utility.ApiConstants
import okhttp3.*
import okio.ByteString
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import org.json.JSONObject


class MainListFragment : ScopedFragment(), KodeinAware, OnMenuCardItemClickedListener, CardClickListener {

    override val kodein by closestKodein()
    private val viewModelFactory: MainListViewModelFactory by instance()
    private lateinit var viewModel: MainListViewModel
    private lateinit var adapter: MainListAdapter
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var scrollListener: EndlessScrollListener

    private lateinit var client : OkHttpClient

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
        startWS()
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
                            this.loading = false
                            Log.e("Error", "Fetch data failed: ${it.message}")
                            progressBar.visibility = View.GONE
                        }, {
                            this.loading = false
                            Log.e("Error", "Fetched data finished")
                            progressBar.visibility = View.GONE
                        })
                        .addTo(compositeDisposable)
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }
    override fun onDeletedClicked(note: NoteDomainModel) {
        (activity as? MainActivity)?.showrConfirmationlAlertDialog("Confirm", "Are you sure?", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                viewModel.deleteEntry(note)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({}, {
                            Log.e("ERROR", "Delete request failed")
                            Toast.makeText(ExamenApp.applicationContext(), "Delete failed " ,Toast.LENGTH_SHORT).show()
                        }, {
                            Log.i("SUCCES", "Delete request succeeded")
                            Toast.makeText(ExamenApp.applicationContext(), "Delete suc " ,Toast.LENGTH_SHORT).show()
                        })
            }
        })
    }
    override fun onDeleteClicked(entry: GlucoseEntry) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditClicked(glucoseEntry: GlucoseEntry) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun startWS() {
        client = OkHttpClient()
        val request = Request.Builder().url(ApiConstants.BASE_URL).build()
        val listener = CustomWebSocketListener()
        val ws = client.newWebSocket(request, listener)
        client.dispatcher().executorService().shutdown()
    }

    class CustomWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            output(t.message.toString())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)

            //val parser = Parser.default()
            //val json = parser.parse(text)

            val json = getJsonFromString(text)

            if (json["event"]?.equals("inserted")!!) {
                val id = (json["note"] as JsonObject).lookup<Int?>("id")
                val text = (json["note"] as JsonObject).lookup<String?>("text")
                val date = (json["note"] as JsonObject).lookup<String?>("date")
                val note = NoteDomainModel(id.get(0)!!, text.get(0)!!, date.get(0)!!)
                output(note.toString())
            }
        }

        fun getJsonFromString(text: String) : JsonObject {
            val parser  = Parser.default()
            val stringBuilder = StringBuilder(text)
            return parser.parse(stringBuilder) as JsonObject
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            output(bytes.hex())
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
        }

        fun output(txt: String) {
            doAsync {
                Log.e("WEB SOCKET", txt.toString())
            }
        }
    }
}
