package com.gluco.Presentation.MainList

import android.content.DialogInterface
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Presentation.MainActivity
import com.gluco.Presentation.ScopedFragment

import com.gluco.R
import com.gluco.Utility.getDateTimeFromTimestamp
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.glucose_entry_edit_fragment.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.lang.Exception
import java.util.*
import android.content.Intent
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


class GlucoseEntryEditFragment : ScopedFragment(), KodeinAware {

    var isAdd: Boolean = false
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance() = GlucoseEntryEditFragment()
    }

    override val kodein by closestKodein()
    private val viewModelFactory: MainListViewModelFactory by instance()
    private lateinit var viewModel: MainListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.glucose_entry_edit_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.edit_entry_fragment_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            R.id.saveIcon -> {
                when {
                    isAdd -> addEntry()
                    !isAdd -> updateEntry()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setUpView(entry: GlucoseEntry) {
        valueEditText.setText(entry.value.toString())
        afterMealSwitch.isChecked = entry.afterMeal
        val calendar = Long.getDateTimeFromTimestamp(entry.timestamp!!)
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = calendar.get(Calendar.HOUR)
            timePicker.minute = calendar.get(Calendar.MINUTE)
        } else {
            timePicker.currentHour = calendar.get(Calendar.HOUR)
            timePicker.currentMinute = calendar.get(Calendar.MINUTE)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainListViewModel::class.java)
        viewModel.selectedEntry.observe(this@GlucoseEntryEditFragment, Observer {
            if (it.id == null) {
                isAdd = true
            }
            setUpView(viewModel.selectedEntry.value!!)
        })
    }

    private fun checkForm(): Boolean {
        val isValueNotValid = (valueEditText.text.toString().isEmpty() || valueEditText.text.toString().toInt() !in 0..1000)
        val isDateNotValid = datePicker.year <= 1970
        if (isValueNotValid && isDateNotValid) {
            (activity as? MainActivity)?.showrNeutralAlertDialog("Error", "Date or value are invalid", "OK", DialogInterface.OnClickListener { _, _ ->  })
            return false
        }
        return true
    }

    private fun setEntryProperties() {
        viewModel.selectedEntry.value?.value = valueEditText.text.toString().toInt()
        viewModel.selectedEntry.value?.afterMeal = afterMealSwitch.isChecked

        val year = datePicker.year
        val month = datePicker.month
        val day = datePicker.dayOfMonth
        var hour = 0
        var min = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.hour
            min = timePicker.minute
        } else {
            hour = timePicker.currentHour
            min = timePicker.currentMinute
        }
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, min)
        viewModel.selectedEntry.value?.timestamp = calendar.timeInMillis
    }

    private fun addEntry() {
        if (checkForm()) {
            setEntryProperties()
            viewModel.addEntry(viewModel.selectedEntry.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    Log.e("ERROR", "Add entry request failed: ${it.message}")
                }, {
                    (activity as? MainActivity)?.showToast("Entry added")
                    Log.e("SUCCES", "Add entry request succeeded")
                    findNavController().navigateUp()
                })
                .addTo(compositeDisposable)
        }
        if (viewModel.selectedEntry.value?.value !in 25 .. 350) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"


            val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
            val mail = prefs.getString("EMERGENCY_EMAIL", "ghimpulucianeduard@gmail.com")

            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            intent.putExtra(Intent.EXTRA_SUBJECT, "URGENT")
            intent.putExtra(Intent.EXTRA_TEXT, "Low glucose lvl: $viewModel.selectedEntry.value?.value.toString()")
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."))
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun updateEntry() = launch(Dispatchers.IO) {
        if (checkForm()) {
            setEntryProperties()
            viewModel.updateEntry(viewModel.selectedEntry.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    Log.e("ERROR", "Update entry request failed: ${it.message}")
                }, {
                    (activity as? MainActivity)?.showToast("Entry Updated")
                    Log.e("SUCCES", "Update entry request succeeded")
                    findNavController().navigateUp()
                })
                .addTo(compositeDisposable)
        }
    }
}
