package com.gluco.Presentation

import android.content.DialogInterface
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.gluco.ExamenApp
import com.gluco.R
import kotlinx.android.synthetic.main.activity_main.*
import com.gluco.Presentation.MainList.MainListViewModel
import com.gluco.Presentation.MainList.MainListViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener, KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: MainListViewModelFactory by instance()
    private lateinit var viewModel: MainListViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainListViewModel::class.java)
        //registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)


        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHost!!.navController

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)

        graph.startDestination = R.id.mainListFragment
//        navController.addOnNavigatedListener { controller, destination ->
//            when(destination.id) {
//                R.id.loginFragment -> {
//                    val prefs = PreferenceManager.getDefaultSharedPreferences(this)
//                    val token = prefs.getString("AUTH_TOKEN", String.empty())
//                    if (token != String.empty()) {
//                        val action = LoginFragmentDirections.actionLoginFragmentToMainListFragment2()
//                        navController.navigate(action)
//                    }
//                }
//            }
//        }

        navController.graph = graph
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(null, navController)
//    }

    fun showrNeutralAlertDialog(title: String, msg: String, neutBtn: String, list: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setNeutralButton(neutBtn, list)
        builder.create().show()
    }

    fun showrConfirmationlAlertDialog(title: String, msg: String, yesListner: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(android.R.string.yes, yesListner)
        builder.setNegativeButton(android.R.string.no, null)
        builder.create().show()
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(isConnected: Boolean) {
        if (!isConnected) {
            val messageToUser = "You are offline now."
            showToast(messageToUser)
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }
}
