package com.example.fit_connect.ui.dashboard.measurements

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R

class MeasurementFragment : Fragment(R.layout.fragment_measurement) {
    private val viewModel: MeasurementViewModel by viewModels()

    private lateinit var adapter: MeasurementAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.measurementsRecyclerView)
        val adapter = MeasurementAdapter() // Use the adapter with proper type arguments
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Observe the data from ViewModel and submit it to the adapter
        viewModel.measurements.observe(viewLifecycleOwner) { measurements ->
            adapter.submitList(measurements)
        }

        // Navigation to add measurement fragment
        view.findViewById<Button>(R.id.addMeasurementButton).setOnClickListener {
            findNavController().navigate(R.id.action_measurementFragment_to_addMeasurementFragment)
        }
    }
}
