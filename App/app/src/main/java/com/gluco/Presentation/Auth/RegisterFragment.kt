package com.gluco.Presentation.Auth

import android.content.DialogInterface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gluco.Presentation.MainActivity

import com.gluco.R
import com.gluco.Utility.empty
import com.gluco.Utility.isEmailValid
import kotlinx.android.synthetic.main.register_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class RegisterFragment : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: AuthViewModel
    override val kodein by closestKodein()
    private val viewModelFactory: AuthViewModelFactory by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? MainActivity)?.setBottomBarVisibility(false)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthViewModel::class.java)
        bindUI()
    }

    private fun bindUI() {

    }

    private fun isFormValid(): Boolean {
        var msg = String.empty()
        val isEmailValid = String.isEmailValid(emailEditText.text.toString())
        if (!isEmailValid) msg += "Email invalid \n"
        val isPasswordValid = passwordEditText.text.toString().length >= 6
        if (!isPasswordValid) msg += "Password too short \n"
        val isConfirmValid = confirmPassEditText.text.toString().length >= 6
            && passwordEditText.text.toString() == confirmPassEditText.text.toString()
        if (!isConfirmValid) msg += "Passwords do not match"

        if (msg != String.empty()) {
            (activity as? MainActivity)?.showrNeutralAlertDialog("Error", msg, "Ok", DialogInterface.OnClickListener { _, _ -> })
            return false
        }
        return true
    }

}
