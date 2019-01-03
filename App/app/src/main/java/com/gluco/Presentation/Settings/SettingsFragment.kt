package com.gluco.Presentation.Settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.gluco.Presentation.MainActivity
import com.gluco.Presentation.MainList.MainListFragmentDirections
import com.gluco.R
import com.gluco.Utility.empty

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val button = findPreference("LOG_OUT")
        button.setOnPreferenceClickListener {
            val editor = PreferenceManager.getDefaultSharedPreferences(activity).edit()
            val mail = editor.putString("AUTH_TOKEN", String.empty())
            editor.apply()
            val action = SettingsFragmentDirections.goToLogin()
            Navigation.findNavController(activity!!, R.id.nav_host_fragment)
                .navigate(action)
            true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? MainActivity)?.setBottomBarVisibility(true)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }
}