package com.example.fit_connect.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fit_connect.R
import com.example.fit_connect.WorkoutRecord
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var chart: BarChart
    private lateinit var durationButton: Button
    private lateinit var volumeButton: Button
    private lateinit var repsButton: Button
    private lateinit var exercisesButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements
        chart = view.findViewById(R.id.chart)
        durationButton = view.findViewById(R.id.durationButton)
        volumeButton = view.findViewById(R.id.volumeButton)
        repsButton = view.findViewById(R.id.repsButton)
        exercisesButton = view.findViewById(R.id.exercisesButton) // Exercises button

        // Set click listener for navigating to ExercisesFragment
        exercisesButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_exercisesFragment)
        }

        // Set click listeners for buttons to update chart
        durationButton.setOnClickListener {
            updateChart(viewModel.weeklyRecords.value ?: listOf(), "duration")
            toggleButtonColors(durationButton)
        }

        volumeButton.setOnClickListener {
            updateChart(viewModel.weeklyRecords.value ?: listOf(), "volume")
            toggleButtonColors(volumeButton)
        }

        repsButton.setOnClickListener {
            updateChart(viewModel.weeklyRecords.value ?: listOf(), "reps")
            toggleButtonColors(repsButton)
        }

        // Observe weekly records from ViewModel
        viewModel.weeklyRecords.observe(viewLifecycleOwner) { records ->
            updateChart(records, "duration") // Default chart to "duration"
        }
    }

    private fun updateChart(records: List<WorkoutRecord>, metric: String) {
        val entries = ArrayList<BarEntry>()

        // Populate chart entries
        records.forEachIndexed { index, record ->
            val value = when (metric) {
                "duration" -> record.duration.toFloat()
                "volume" -> record.volume.toFloat()
                "reps" -> record.reps.toFloat()
                else -> 0f
            }
            entries.add(BarEntry(index.toFloat(), value))
        }

        // Set up BarDataSet and customize appearance
        val dataSet = BarDataSet(entries, metric.capitalize())
        dataSet.color = resources.getColor(R.color.blue, null)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 10f

        val data = BarData(dataSet)
        chart.data = data
        chart.invalidate() // Refresh the chart
    }

    private fun toggleButtonColors(selectedButton: Button) {
        // Reset all buttons to gray
        durationButton.setBackgroundColor(resources.getColor(R.color.gray, null))
        volumeButton.setBackgroundColor(resources.getColor(R.color.gray, null))
        repsButton.setBackgroundColor(resources.getColor(R.color.gray, null))

        // Highlight the selected button
        selectedButton.setBackgroundColor(resources.getColor(R.color.blue, null))
    }
}
