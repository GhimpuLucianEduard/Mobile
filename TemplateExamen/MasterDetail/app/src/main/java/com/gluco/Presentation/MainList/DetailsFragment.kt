package com.gluco.Presentation.MainList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.gluco.Data.Local.TaskDomainModel
import com.gluco.Presentation.ScopedFragment

import com.gluco.R
import kotlinx.android.synthetic.main.details_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import com.gluco.Presentation.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.textChangedListener


class DetailsFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: MainListViewModelFactory by instance()
    private lateinit var viewModel: MainListViewModel

    companion object {
        fun newInstance() = DetailsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainListViewModel::class.java)
        viewModel.selectedEntry.observe(this@DetailsFragment, Observer {
            setUpView(viewModel.selectedEntry.value!!)
        })

        toolbar.setNavigationOnClickListener {

        }

        saveBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                doAsync {
                    val sel = viewModel.selectedEntry.value
                    viewModel.update(TaskDomainModel(sel!!.id, textEditText.text.toString(), System.currentTimeMillis(), sel!!.version))
                    (activity as? MainActivity)?.showToast("entry updated!")
                }
            }
        })

    }



    private fun setUpView(value: TaskDomainModel) {
        textEditText.setText(value.text)
    }

}
