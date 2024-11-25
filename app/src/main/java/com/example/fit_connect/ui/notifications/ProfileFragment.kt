package com.example.fit_connect.ui.notifications
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fit_connect.R
import com.example.fit_connect.WorkoutRecord
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var chart: BarChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        chart = view.findViewById(/* id = */ R.id.chart)

        // Observe weekly records data from ViewModel
        viewModel.weeklyRecords.observe(viewLifecycleOwner) { records ->
            updateChart(records, "duration")
        }


        val endDate = System.currentTimeMillis()
        val startDate = endDate - 7 * 24 * 60 * 60 * 1000

        // Set button click listeners to update the chart based on selected metric
        view.findViewById<Button>(R.id.durationButton).setOnClickListener {
            viewModel.loadWeeklyData(startDate, endDate)
            updateChart(viewModel.weeklyRecords.value ?: listOf(), "duration")
        }

        view.findViewById<Button>(R.id.volumeButton).setOnClickListener {
            viewModel.loadWeeklyData(startDate, endDate)
            updateChart(viewModel.weeklyRecords.value ?: listOf(), "volume")
        }

        view.findViewById<Button>(R.id.repsButton).setOnClickListener {
            viewModel.loadWeeklyData(startDate, endDate)
            updateChart(viewModel.weeklyRecords.value ?: listOf(), "reps")
        }
    }

    private fun updateChart(records: List<WorkoutRecord>, metric: String) {
        val entries = ArrayList<BarEntry>()

        // Populate data entries based on the metric selected
        records.forEachIndexed { index, record ->
            val value = when (metric) {
                "duration" -> record.duration.toFloat()
                "volume" -> record.volume.toFloat()
                "reps" -> record.reps.toFloat()
                else -> 0f
            }
            entries.add(BarEntry(index.toFloat(), value))
        }

        // Create a dataset and add it to the chart
        val dataSet = BarDataSet(entries, metric.capitalize())
        val data = BarData(dataSet)
        chart.data = data
        chart.invalidate() // Refresh the chart with new data
    }
}