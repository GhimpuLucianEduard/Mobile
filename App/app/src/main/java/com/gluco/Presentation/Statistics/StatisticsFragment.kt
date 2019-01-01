package com.gluco.Presentation.Statistics

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gluco.Presentation.MainList.MainListViewModel
import com.gluco.Presentation.MainList.MainListViewModelFactory

import com.gluco.R
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.statistics_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class StatisticsFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: MainListViewModelFactory by instance()
    private lateinit var viewModel: MainListViewModel

    companion object {
        fun newInstance() = StatisticsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainListViewModel::class.java)
        buildChart()
        // TODO: Use the ViewModel
    }

    private fun buildChart() {
        val size = viewModel.entries.value?.size
        val series = LineGraphSeries<DataPoint>()
        val calendar = Calendar.getInstance()
        viewModel.entries.value?.forEach {
            calendar.timeInMillis = it.timestamp!!
            val date = calendar.time
            series.appendData(DataPoint(date, it.value.toDouble()), false, size!!)
        }

        // set date label formatter
        graphView.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)
        graphView.gridLabelRenderer.numHorizontalLabels = 3

        val minTimestamp = viewModel.entries.value?.minBy { it.timestamp!! }?.timestamp
        val maxTimestamp = viewModel.entries.value?.maxBy { it.timestamp!! }?.timestamp

        calendar.timeInMillis = minTimestamp!!
        val minDate = calendar.time
        graphView.viewport.setMinX(minDate.time.toDouble())

        calendar.timeInMillis = maxTimestamp!!
        val maxDate = calendar.time
        graphView.viewport.setMaxX(maxDate.time.toDouble())

        graphView.getViewport().setXAxisBoundsManual(true);

        graphView.viewport.setMinY(0.0)
        graphView.viewport.setMaxY(500.0)
        graphView.viewport.isYAxisBoundsManual = true
        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graphView.gridLabelRenderer.setHumanRounding(false)
        graphView.getViewport().setScrollable(true) // enables horizontal scrolling
        graphView.addSeries(series)
    }
}
