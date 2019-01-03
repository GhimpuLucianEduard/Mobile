package com.gluco.Presentation.Auth

import android.content.DialogInterface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.gluco.Presentation.MainActivity
import com.gluco.Presentation.MainList.MainListViewModel
import com.gluco.Presentation.MainList.MainListViewModelFactory

import com.gluco.R
import com.gluco.Utility.empty
import com.gluco.Utility.isEmailValid
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_fragment.*
import org.jetbrains.anko.Android
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class LoginFragment : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: AuthViewModel
    override val kodein by closestKodein()
    private val viewModelFactory: AuthViewModelFactory by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? MainActivity)?.setBottomBarVisibility(false)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthViewModel::class.java)
        bindUI()
    }

    private fun bindUI() {
        loginBtn.onClick {
            if (isFormValid()) {
                viewModel.login(emailEditText.text.toString(), passwordEditText.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.e("SUCCES", "Login ok ${it}")
                        val editor = PreferenceManager.getDefaultSharedPreferences(activity).edit()
                        val mail = editor.putString("AUTH_TOKEN", it.token)
                        editor.apply()
                    }, {
                        Log.e("ERROR", "Login failed: ${it.message}")
                        (activity as? MainActivity)?.showToast("Login failed")
                    }, {})
            }
        }
    }

    private fun isFormValid(): Boolean {
        var msg = String.empty()
        val isEmailValid = String.isEmailValid(emailEditText.text.toString())
        if (!isEmailValid) msg += "Email invalid \n"
        val isPasswordValid = passwordEditText.text.toString().length >= 6
        if (!isPasswordValid) msg += "Password too short"
        if (msg != String.empty()) {
            (activity as? MainActivity)?.showrNeutralAlertDialog("Error", msg, "Ok", DialogInterface.OnClickListener { _, _ -> })
            return false
        }
        return true
    }

}
