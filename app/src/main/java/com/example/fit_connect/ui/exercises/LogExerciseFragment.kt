package com.example.fit_connect.ui.exercises

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fit_connect.R
import com.example.fit_connect.databinding.FragmentLogExerciseBinding

class LogExerciseFragment : Fragment(R.layout.fragment_log_exercise) {

    private var _binding: FragmentLogExerciseBinding? = null
    private val binding get() = _binding!!

    private val args: LogExerciseFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLogExerciseBinding.bind(view)

        // Set the exercise name from the arguments
        binding.exerciseName.text = args.exerciseName

        // Handle back button click
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Handle finish button click
        binding.finishButton.setOnClickListener {
            // Implement finish workout logic
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}