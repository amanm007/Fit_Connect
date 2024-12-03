package com.example.fit_connect.ui.notifications
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fit_connect.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import androidx.navigation.fragment.findNavController

import androidx.navigation.fragment.findNavController
class WorkoutFragment : Fragment(R.layout.workout_front_page) {

    private lateinit var viewModel: WorkoutViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)

        // Setup click listeners for all interactive elements
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        // Quick Start
        view.findViewById<MaterialCardView>(R.id.startEmptyWorkout).setOnClickListener {
            // Handle empty workout start
            startEmptyWorkout()
        }

        // Routine Buttons
        view.findViewById<MaterialButton>(R.id.newRoutine).setOnClickListener {
            // Handle new routine creation
            createNewRoutine()
        }

        view.findViewById<MaterialButton>(R.id.explore).setOnClickListener {
            // Handle explore action
            exploreRoutines()
        }

        // Workout Routines
        view.findViewById<MaterialCardView>(R.id.chestDayRoutine).setOnClickListener {
            // Handle chest day routine
            startWorkoutRoutine("CHEST DAY")
        }

        view.findViewById<MaterialCardView>(R.id.shoulderDayRoutine).setOnClickListener {
            // Handle shoulder day routine
            startWorkoutRoutine("SHOULDER DAY")
        }

        view.findViewById<MaterialCardView>(R.id.legDayRoutine).setOnClickListener {
            // Handle leg day routine
            startWorkoutRoutine("LEG DAY")
        }

        view.findViewById<MaterialButton>(R.id.startChestRoutineButton).setOnClickListener {
            try {
                val directions = WorkoutFragmentDirections
                    .actionNavigationProfileToExercisesFragment(routineType = "Chest")
                findNavController().navigate(directions)
            } catch (e: Exception) {
                Log.e("WorkoutFragment", "Navigation failed", e)
                Toast.makeText(context, "Navigation failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<MaterialButton>(R.id.startShoulderRoutineButton).setOnClickListener {
            try {
                val directions = WorkoutFragmentDirections
                    .actionNavigationProfileToExercisesFragment(routineType = "Shoulder")
                findNavController().navigate(directions)
            } catch (e: Exception) {
                Log.e("WorkoutFragment", "Navigation failed", e)
                Toast.makeText(context, "Navigation failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<MaterialButton>(R.id.startLegRoutineButton).setOnClickListener {
            try {
                val directions = WorkoutFragmentDirections
                    .actionNavigationProfileToExercisesFragment(routineType = "Leg")
                findNavController().navigate(directions)
            } catch (e: Exception) {
                Log.e("WorkoutFragment", "Navigation failed", e)
                Toast.makeText(context, "Navigation failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startEmptyWorkout() {
        try {
            viewModel.startEmptyWorkout()
            findNavController().navigate(R.id.action_navigation_profile_to_emptyWorkoutFragment)
        } catch (e: Exception) {
            Log.e("WorkoutFragment", "Navigation failed", e)
            Toast.makeText(context, "Navigation failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNewRoutine() {
        // Implement new routine creation logic
        viewModel.createNewRoutine()
    }

    private fun exploreRoutines() {
        // Implement explore routines logic
        viewModel.exploreRoutines()
    }

    private fun startWorkoutRoutine(routineName: String) {
        // Implement routine start logic
        viewModel.startWorkoutRoutine(routineName)
    }
}