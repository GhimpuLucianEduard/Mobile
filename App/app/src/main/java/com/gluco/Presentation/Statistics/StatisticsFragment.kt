package com.gluco.Presentation.Statistics

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gluco.Presentation.MainActivity
import com.gluco.Presentation.MainList.MainListViewModel
import com.gluco.Presentation.MainList.MainListViewModelFactory

import com.gluco.R
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.statistics_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import com.jjoe64.graphview.series.BarGraphSeries
import kotlin.collections.HashMap
import com.jjoe64.graphview.ValueDependentColor




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
        val size = viewModel.entries.value?.size
        if (size != null && size >= 2) {
            buildChart()
            buildBarChart()
        }
        (activity as? MainActivity)?.setBottomBarVisibility(true)
        // TODO: Use the ViewModel
    }

    private fun buildBarChart() {
//        0-50
//        51-100
//        101-150
//        151-200
//        200-250
//        250+
        val range = mutableMapOf<Int, Int>()
        for (i in 0..5) {
            range[i] = 0
        }
        var series = BarGraphSeries<DataPoint>()
        viewModel.entries.value?.forEach {
            when {
                it.value in 0..50 -> range.set(0, range.getValue(0) + 1)
                it.value in 51..100 -> range.set(1, range.getValue(1) + 1)
                it.value in 101..150 -> range.set(2, range.getValue(2) + 1)
                it.value in 151..200 -> range.set(3, range.getValue(3) + 1)
                it.value in 201..250 -> range.set(4, range.getValue(4) + 1)
                else -> range.set(5, range.getValue(5) + 1)
            }
        }

        range.forEach {
            series.appendData(DataPoint(it.key.toDouble(), it.value.toDouble()), false, 7)
        }

        series.valueDependentColor = ValueDependentColor { data -> Color.rgb(data.x.toInt() * 255 / 4, Math.abs(data.y * 255 / 6).toInt(), 100) }
        series.spacing = 50
        series.isDrawValuesOnTop = true
        series.valuesOnTopColor = Color.RED
        barChart.addSeries(series)
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
        series.color = Color.CYAN
        series.setAnimated(true)
    }
}
