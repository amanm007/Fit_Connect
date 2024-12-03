package com.example.fit_connect.ui.workout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fit_connect.R
import com.google.android.material.button.MaterialButton

class EmptyWorkoutFragment : Fragment(R.layout.empty_workout_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the data from the Bundle
        val exerciseName = arguments?.getString("exerciseName")
        val exerciseCategory = arguments?.getString("exerciseCategory")

        // Setup click listeners
        view.findViewById<View>(R.id.backButton).setOnClickListener {
            findNavController().navigateUp()
        }

        view.findViewById<MaterialButton>(R.id.addExerciseButton).setOnClickListener {
            findNavController().navigate(R.id.action_emptyWorkoutFragment_to_exercisesFragment)
        }

        view.findViewById<View>(R.id.settingsButton).setOnClickListener {
            // Handle settings
        }

        view.findViewById<View>(R.id.discardButton).setOnClickListener {
            findNavController().navigateUp()
        }

        view.findViewById<View>(R.id.finishButton).setOnClickListener {
            // Handle finish workout
        }
    }
}