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
import com.example.fit_connect.ui.UserActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private val user_id = "USER_ID"
    private val viewModel: DashboardViewModel by viewModels {
        val bundle = (activity as? UserActivity)?.sharedBundle
        val userId = bundle!!.getLong(user_id)
        DashboardViewModelFactory(requireActivity().application, userId)
    }

    private lateinit var chart: BarChart
    private lateinit var durationButton: Button
    private lateinit var volumeButton: Button
    private lateinit var repsButton: Button
    private lateinit var exercisesButton: Button
    private lateinit var calendarButton: Button
    private lateinit var measuresButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements
        chart = view.findViewById(R.id.chart)
        durationButton = view.findViewById(R.id.durationButton)
        volumeButton = view.findViewById(R.id.volumeButton)
        repsButton = view.findViewById(R.id.repsButton)
        exercisesButton = view.findViewById(R.id.exercisesButton)
        calendarButton=view.findViewById(R.id.calendarButton)
        measuresButton=view.findViewById(R.id.measuresButton)



        // Set click listener for navigating to ExercisesFragment
        exercisesButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_exercisesFragment)
        }

        calendarButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_calendarFragment)
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
        measuresButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_measurementFragment)
        }


        // Observe weekly records from ViewModel
        viewModel.weeklyRecords.observe(viewLifecycleOwner) { records ->
            updateChart(records, "duration") // Default chart to "duration"
        }
    }

    private fun updateChart(records: List<WorkoutRecord>, metric: String) {
        val entries = ArrayList<BarEntry>()
        val fakeData = when (metric) {
            "duration" -> listOf(2f, 4f, 3f, 6f, 5f, 7f, 4f) // Hours for each day
            "volume" -> listOf(1000f, 1500f, 1200f, 2000f, 1800f, 2500f, 2300f) // Weight in pounds
            "reps" -> listOf(50f, 80f, 65f, 90f, 70f, 100f, 85f) // Total reps
            else -> listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f) // Default to empty data
        }

        // Add data points to the chart
        fakeData.forEachIndexed { index, value ->
            entries.add(BarEntry(index.toFloat(), value))
        }

        val dataSet = BarDataSet(entries, metric.capitalize())
        dataSet.color = resources.getColor(R.color.blue, null)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 10f

        val data = BarData(dataSet)
        chart.data = data
        chart.description.isEnabled = false
        chart.axisLeft.textColor = Color.WHITE
        chart.axisRight.textColor = Color.WHITE
        chart.xAxis.textColor = Color.WHITE
        chart.legend.textColor = Color.WHITE

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
