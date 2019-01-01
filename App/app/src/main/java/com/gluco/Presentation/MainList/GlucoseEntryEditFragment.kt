package com.gluco.Presentation.MainList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gluco.R

class GlucoseEntryEditFragment : Fragment() {

    companion object {
        fun newInstance() = GlucoseEntryEditFragment()
    }

    private lateinit var viewModel: GlucoseEntryEditViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.glucose_entry_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GlucoseEntryEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
