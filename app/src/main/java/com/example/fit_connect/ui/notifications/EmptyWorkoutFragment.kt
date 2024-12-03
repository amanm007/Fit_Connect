package com.example.fit_connect.ui.workout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.data.workout.WorkoutRepository
import com.example.fit_connect.databinding.FragmentWorkoutHistoryBinding
import com.example.fit_connect.ui.UserActivity
import com.example.fit_connect.ui.notifications.WorkoutHistory
import com.example.fit_connect.ui.notifications.WorkoutHistoryAdapter
import com.google.android.material.button.MaterialButton
import java.util.Date

class EmptyWorkoutFragment : Fragment(R.layout.empty_workout_page) {
    private var _binding: FragmentWorkoutHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FitConnectDatabase
    private lateinit var workoutRepo: WorkoutRepository
    private lateinit var userRepo: UserRepository

    private val user_id = "USER_ID"
    private var userId : Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FitConnectDatabase.getInstance(requireContext())
        workoutRepo = WorkoutRepository(db.workoutDao())
        userRepo = UserRepository(db.userDao())

        val bundle = (activity as? UserActivity)?.sharedBundle
        userId = bundle!!.getLong(user_id)

        // Retrieve the data from the Bundle
        val exerciseName = arguments?.getString("exerciseName")
        val exerciseCategory = arguments?.getString("exerciseCategory")

        val recyclerView = view.findViewById<RecyclerView>(R.id.exercisesRecyclerView)

        userRepo.getUserWithSimpleWorkouts(userId).observe(viewLifecycleOwner) {
            println("GET USER WITH SIMPLE WORKOUTS")
            it.workouts.forEach { w -> println(w) }
            recyclerView.apply {
                val workoutHistories = it.workouts.map { w ->
                    WorkoutHistory(
                        time = Date(w.timestamp),
                        duration = w.duration.toLong()
                    )
                }
                layoutManager = LinearLayoutManager(requireContext())
                adapter = WorkoutHistoryAdapter(workoutHistories)
            }
        }

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

    private fun setupRecyclerView() {

    }
}