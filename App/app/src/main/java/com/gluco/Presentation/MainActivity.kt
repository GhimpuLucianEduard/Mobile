package com.gluco.Presentation

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.gluco.R
import kotlinx.android.synthetic.main.activity_main.*
import androidx.navigation.NavGraph
import androidx.navigation.NavInflater
import androidx.preference.PreferenceManager
import com.gluco.Presentation.Auth.LoginFragmentDirections
import com.gluco.Presentation.MainList.MainListFragmentDirections
import com.gluco.Utility.empty


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)


        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHost!!.navController

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)

        graph.startDestination = R.id.loginFragment
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

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(null, navController)
    }

    fun showrNeutralAlertDialog(title: String, msg: String, neutBtn: String, list: DialogInterface.OnClickListener) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setNeutralButton(neutBtn, list)
        builder.create().show()
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun setBottomBarVisibility(bool: Boolean) {
        bottom_nav.visibility = if (bool) View.VISIBLE else View.GONE
    }
}
