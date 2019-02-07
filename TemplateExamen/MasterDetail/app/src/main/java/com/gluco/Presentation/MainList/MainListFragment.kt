package com.gluco.Presentation.MainList

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Presentation.MainActivity
import com.gluco.Presentation.ScopedFragment
import com.gluco.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_list_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.lookup
import com.gluco.Data.Local.TaskDomainModel
import com.gluco.ExamenApp
import com.gluco.Utility.ApiConstants
import okhttp3.*
import okio.ByteString
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync


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
        //startWS()
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
    }
    override fun onDeletedClicked(task: TaskDomainModel) {
//        (activity as? MainActivity)?.showrConfirmationlAlertDialog("Confirm", "Are you sure?", object : DialogInterface.OnClickListener {
//            override fun onClick(dialog: DialogInterface?, which: Int) {
//                viewModel.deleteEntry(task)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe({}, {
//                            Log.e("ERROR", "Delete request failed")
//                            Toast.makeText(ExamenApp.applicationContext(), "Delete failed " ,Toast.LENGTH_SHORT).show()
//                        }, {
//                            Log.i("SUCCES", "Delete request succeeded")
//                            Toast.makeText(ExamenApp.applicationContext(), "Delete suc " ,Toast.LENGTH_SHORT).show()
//                        })
//            }
//        })
    }
    override fun onDeleteClicked(entry: TaskDomainModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditClicked(entry: TaskDomainModel) {
        viewModel.selectedEntry.value = entry
        val action = MainListFragmentDirections.actionMainListFragmentToDetailsFragment()
        Navigation.findNavController(activity!!, R.id.nav_host_fragment)
            .navigate(action)
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
//
//            if (json["event"]?.equals("inserted")!!) {
//                val id = (json["note"] as JsonObject).lookup<Int?>("id")
//                val text = (json["note"] as JsonObject).lookup<String?>("text")
//                val date = (json["note"] as JsonObject).lookup<String?>("date")
//                val note = TaskDomainModel(id.get(0)!!, text.get(0)!!, date.get(0)!!)
//                output(note.toString())
//            }
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
