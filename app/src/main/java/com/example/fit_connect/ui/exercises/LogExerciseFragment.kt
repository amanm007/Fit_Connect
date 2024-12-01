package com.example.fit_connect.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R
import com.example.fit_connect.databinding.FragmentLogExerciseBinding
import com.example.fit_connect.databinding.ItemWorkoutSetBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class LogExerciseFragment : Fragment() {
    private var _binding: FragmentLogExerciseBinding? = null
    private val binding get() = _binding!!
    private lateinit var setsAdapter: WorkoutSetsAdapter
    private val args: LogExerciseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exerciseName.text = args.exerciseName
        setupRecyclerView()
        setupButtons()
    }

    private fun setupRecyclerView() {
        setsAdapter = WorkoutSetsAdapter()
        binding.setsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = setsAdapter
        }
    }

    private fun setupButtons() {
        binding.apply {
            addSetButton.setOnClickListener { showAddSetDialog() }
            addExerciseButton.setOnClickListener {
                findNavController().navigate(
                    LogExerciseFragmentDirections.actionLogExerciseFragmentToExercisesFragment()
                )
            }
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
            finishButton.setOnClickListener {
                // Save workout and navigate back
                findNavController().navigateUp()
            }
            discardButton.setOnClickListener {
                showDiscardWorkoutDialog()
            }
        }
    }

    private fun showAddSetDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_set, null)

        val weightInput = dialogView.findViewById<TextInputEditText>(R.id.weightInput)
        val repsInput = dialogView.findViewById<TextInputEditText>(R.id.repsInput)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Set")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val weight = weightInput.text?.toString()?.toFloatOrNull() ?: 0f
                val reps = repsInput.text?.toString()?.toIntOrNull() ?: 0
                if (weight > 0 && reps > 0) {
                    setsAdapter.addSet(weight, reps)  // Changed to match the adapter's method
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDiscardWorkoutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Discard Workout")
            .setMessage("Are you sure you want to discard this workout?")
            .setPositiveButton("Discard") { _, _ ->
                findNavController().navigateUp()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}