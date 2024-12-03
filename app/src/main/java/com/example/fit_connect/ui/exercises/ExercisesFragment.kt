package com.example.fit_connect.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.workout.ExerciseType
import com.example.fit_connect.data.workout.WorkoutRepository
import com.example.fit_connect.databinding.FragmentExercisesBinding
import com.google.android.material.textfield.TextInputEditText

class ExercisesFragment : Fragment() {
    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FitConnectDatabase
    private lateinit var workoutRepo: WorkoutRepository
    private lateinit var allExercises: List<ExerciseType>
    private lateinit var filteredExercises: List<ExerciseType>

    private val args: ExercisesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)

        db = FitConnectDatabase.getInstance(requireContext())
        workoutRepo = WorkoutRepository(db.workoutDao())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupSearchButton()

        workoutRepo.getAllExerciseTypes().observe(viewLifecycleOwner) { exercises ->
            allExercises = exercises
            filteredExercises = filterExercisesByRoutineType(exercises)
            setupRecyclerView()
        }
    }

    private fun filterExercisesByRoutineType(exercises: List<ExerciseType>): List<ExerciseType> {
        return when (args.routineType) {
            "Chest" -> exercises.filter {
                it.type.displayName in listOf("Bench Press", "Push Ups")
            }
            "Shoulder" -> exercises.filter {
                it.type.displayName in listOf("Overhead Press", "Dumbbell Curl")
            }
            "Leg" -> exercises.filter {
                it.type.displayName in listOf("Squat", "Dead Lift")
            }
            "Back" -> exercises.filter {
                it.type.displayName in listOf("Dead Lift", "Pull Ups", "Barbell Row")
            }
            else -> exercises
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = "${args.routineType} Exercises"
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.exercisesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ExercisesAdapter(filteredExercises) { exercise ->
                findNavController().navigate(
                    ExercisesFragmentDirections.actionExercisesFragmentToLogExerciseFragment(
                        exercise.type.displayName,
                        exercise.exerciseTypeId!!
                    )
                )
            }
        }
    }

    private fun setupSearchButton() {
        binding.searchButton.setOnClickListener {
            showSearchDialog()
        }
    }

    private fun showSearchDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_search_exercise, null)
        val searchInput = dialogView.findViewById<TextInputEditText>(R.id.search_input)

        AlertDialog.Builder(requireContext())
            .setTitle("Search Exercises")
            .setView(dialogView)
            .setPositiveButton("Search") { _, _ ->
                val query = searchInput.text.toString()
                filterExercises(query)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun filterExercises(query: String) {
        val routineFilteredExercises = filterExercisesByRoutineType(allExercises)

        filteredExercises = if (query.isEmpty()) {
            routineFilteredExercises
        } else {
            routineFilteredExercises.filter { exercise ->
                exercise.type.displayName.contains(query, ignoreCase = true) ||
                        exercise.type.category.contains(query, ignoreCase = true)
            }
        }
        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        binding.exercisesRecyclerView.adapter = ExercisesAdapter(filteredExercises) { exercise ->
            findNavController().navigate(
                ExercisesFragmentDirections.actionExercisesFragmentToLogExerciseFragment(
                    exercise.type.displayName,
                    exercise.exerciseTypeId!!
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}