package com.example.fit_connect.ui.home.nested_fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.UserDao
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.data.workout.Exercise
import com.example.fit_connect.data.workout.WorkoutDao
import com.example.fit_connect.data.workout.WorkoutRepository
import com.example.fit_connect.databinding.FragmentNestedPrhistoryBinding
import com.example.fit_connect.ui.UserActivity
import com.example.fit_connect.ui.home.nested_fragments.listadapters.PRListAdapter


class NestedPRHistoryFragment: Fragment() {

    private var _binding: FragmentNestedPrhistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val user_id = "USER_ID"
    private var userId : Long = 0

    private lateinit var database : FitConnectDatabase
    private lateinit var userdao : UserDao
    private lateinit var userRepository: UserRepository
    private lateinit var workoutdao : WorkoutDao
    private lateinit var workoutRepository: WorkoutRepository

    private var exerciseHistoryList : MutableList<Exercise> = mutableListOf()
    private lateinit var arrayAdapter : PRListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentNestedPrhistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bundle = (activity as? UserActivity)?.sharedBundle
        userId = bundle!!.getLong(user_id)

        database = FitConnectDatabase.getInstance(requireContext())
        userdao = database.userDao()
        userRepository = UserRepository(userdao)
        workoutdao = database.workoutDao()
        workoutRepository = WorkoutRepository(workoutdao)

        setPRHistory(root)
        return root
    }
    private fun setPRHistory(root : View){
        val prhistoryListView = root.findViewById<ListView>(R.id.pr_history_listview)

        arrayAdapter = PRListAdapter(requireContext(), exerciseHistoryList)
        prhistoryListView.adapter = arrayAdapter

        //Observe (Yes its nested. It looks gross but i need to get all exercises)
        val liveWorkoutList = userRepository.getUserWithSimpleWorkouts(userId)
        liveWorkoutList.observe(requireContext() as LifecycleOwner) {
            workoutlist ->
            exerciseHistoryList = mutableListOf()
            if (workoutlist != null) {
                for (workout in workoutlist.workouts) {
                    val liveExerciseList = workoutRepository.getWorkoutWithExercises(workout.workoutId!!)
                    liveExerciseList.observe(requireContext() as LifecycleOwner){
                        exerciselist ->
                        if(exerciselist != null){
                            for(exercise in exerciselist.exercises){
                                exerciseHistoryList.add(exercise)
                            }
                        }
                    }
                }
            }
            arrayAdapter.replace(exerciseHistoryList)
            arrayAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}