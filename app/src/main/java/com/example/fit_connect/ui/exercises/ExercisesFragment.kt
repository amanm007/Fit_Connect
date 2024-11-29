package com.example.fit_connect.ui.exercises

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fit_connect.R
import com.example.fit_connect.databinding.FragmentExercisesBinding

class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ExercisesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentExercisesBinding.bind(view)


        binding.toolbar.setNavigationOnClickListener {
            if (!findNavController().navigateUp()) {
                requireActivity().onBackPressed()
            }
        }

        val exercisesList = listOf(
            Exercise("Pull Up (Weighted)", "Back"),
            Exercise("Standing Calf Raises (Machine)", "Legs"),
            Exercise("Leg Extensions (Machine)", "Legs"),
            Exercise("Hammer Curl (Dumbbell)", "Arms"),
            Exercise("Squat (Barbell)", "Legs"),
            Exercise("Bicep Curl (Dumbbell)", "Arms"),
            Exercise("Lat Pulldown (Single Arm)", "Back"),
            Exercise("Seated Row (Machine)", "Back")
        )

        adapter = ExercisesAdapter(exercisesList) { exercise ->
            // Create a Bundle to pass the data
            val bundle = Bundle().apply {
                putString("exerciseName", exercise.name)
                putString("exerciseCategory", exercise.category)
            }

            // Navigate to EmptyWorkoutFragment and pass the bundle
            findNavController().navigate(R.id.action_exercisesFragment_to_emptyWorkoutFragment, bundle)
        }



        binding.exercisesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.exercisesRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
