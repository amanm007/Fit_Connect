package com.example.fit_connect.ui.dashboard

import android.graphics.Color
import android.icu.util.Calendar
import android.media.Image
import android.os.Bundle
import android.provider.ContactsContract.Contacts.Photo
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.anychart.core.gauge.pointers.Bar
import com.example.fit_connect.PhotoUtil
import com.example.fit_connect.R
import com.example.fit_connect.WorkoutRecord
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.UserDao
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.data.workout.ExerciseWithSets
import com.example.fit_connect.data.workout.Workout
import com.example.fit_connect.data.workout.WorkoutDao
import com.example.fit_connect.data.workout.WorkoutRepository
import com.example.fit_connect.helperMapWorkoutRecords
import com.example.fit_connect.ui.UserActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import org.w3c.dom.Text

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

    private var userId : Long = 0

    //Repository
    private lateinit var database : FitConnectDatabase
    private lateinit var workoutDao: WorkoutDao
    private lateinit var userDao: UserDao
    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var userRepository: UserRepository

    //Dashboard Weekly Stats
    private var durationStats : MutableList<Int> = mutableListOf(0,0,0,0,0,0,0)
    private var repsStats : MutableList<Int> = mutableListOf(0,0,0,0,0,0,0)
    private var volumeStats : MutableList<Int> = mutableListOf(0,0,0,0,0,0,0)

    //Calendar Get Day of the Week
    val calendar = Calendar.getInstance()
    val start_dayofWeekIndex = calendar.get(Calendar.DAY_OF_WEEK) % 7
    val weekdays = listOf(
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    )
    private lateinit var recordslive : LiveData<Map<Workout, List<ExerciseWithSets>>>
    private lateinit var volumeTxt : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements
        val bundle = (activity as? UserActivity)?.sharedBundle
        userId = bundle!!.getLong(user_id)

        chart = view.findViewById(R.id.chart)
        durationButton = view.findViewById(R.id.durationButton)
        volumeButton = view.findViewById(R.id.volumeButton)
        repsButton = view.findViewById(R.id.repsButton)
        exercisesButton = view.findViewById(R.id.exercisesButton)
        calendarButton=view.findViewById(R.id.calendarButton)
        volumeTxt = view.findViewById(R.id.dashboard_workout_volume_value)

        //Initialize Repository
        database = FitConnectDatabase.getInstance(requireContext())
        workoutDao = database.workoutDao()
        userDao = database.userDao()
        workoutRepository = WorkoutRepository(workoutDao)
        userRepository = UserRepository(userDao)

        //Preston Setting User Profile + Workout
        setProfile(view)
        //Preston Setting Observe Duration Data
        getDurationBarData(view)

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

        // Observe weekly records from ViewModel
        viewModel.weeklyRecords.observe(viewLifecycleOwner) { records ->
            updateChart(records, "duration") // Default chart to "duration"
        }
    }

    private fun updateChart(records: List<WorkoutRecord>, metric: String) {
        val entries = ArrayList<BarEntry>()

        // Populate chart entries
        /*
        if(metric == "duration"){
            for(i in 0 .. 6){
                entries.add(BarEntry(i.toFloat(), durationStats[i].toFloat()/60))
            }
        }
        */
        records.forEachIndexed { index, record ->
                val value = when (metric) {
                    "duration" -> record.duration.toFloat()
                    "volume" -> record.volume.toFloat()
                    "reps" -> record.reps.toFloat()
                    else -> 0f
                }
            entries.add(BarEntry(index.toFloat(), value))
        }


        // Set up BarDataSet and customize appearance
        val dataSet = BarDataSet(entries, metric.capitalize())
        dataSet.color = resources.getColor(R.color.blue, null)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 10f

        //Set up X Axis Weekday Labels
        val weekdayOrder : MutableList<String> = mutableListOf()
        for(i in 0 .. 6){
            weekdayOrder.add(weekdays[(start_dayofWeekIndex-1 + i) % 7])
        }
        val xAxis : XAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(weekdayOrder)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.labelCount = 7
        xAxis.textColor = Color.WHITE


        val yAxisRight : YAxis = chart.axisRight
        yAxisRight.isEnabled = false

        val data = BarData(dataSet)
        chart.data = data
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

    private fun setProfile(view: View){
        //Profile Img and Stats
        val username : TextView = view.findViewById(R.id.username)
        val profileImg : ImageView = view.findViewById(R.id.profile_picture)
        val profileName : TextView = view.findViewById(R.id.profile_name)
        val workoutcount : TextView = view.findViewById(R.id.workout_counts_txt)
        val followers : TextView = view.findViewById(R.id.follower_counts_txt)
        val following : TextView = view.findViewById(R.id.following_counts_txt)

        //Workout Data and Stats
        val workoutlayout : LinearLayout = view.findViewById(R.id.dashboard_workouts_layout)
        val workoutImg : ImageView = view.findViewById(R.id.dashboard_workouts_img)
        val workoutName : TextView = view.findViewById(R.id.dashboard_workouts_name)
        val workouttime : TextView = view.findViewById(R.id.dashboard_workout_time_value)
        val workoutvolume : TextView = view.findViewById(R.id.dashboard_workout_volume_value)
        val workoutlikes : TextView = view.findViewById(R.id.dashboard_workout_likes)
        val commentImg : ImageView = view.findViewById(R.id.dashboard_workout_comment_img)
        val comment : TextView = view.findViewById(R.id.dashboard_workout_comment)

        val userLiveData = userRepository.getUser(userId)
        userLiveData.observe(requireContext() as LifecycleOwner){
            user ->
            if(user != null){
                username.text = user.userName
                workoutName.text = user.userName
                profileName.text = user.firstName + " " + user.lastName

                if(user.imageData != null){
                    profileImg.setImageBitmap(PhotoUtil.byteArrayToBitmap(user.imageData!!))
                    workoutImg.setImageBitmap(PhotoUtil.byteArrayToBitmap(user.imageData!!))
                }
                else{
                    profileImg.setImageResource(R.drawable.ic_launcher_background)
                    workoutImg.setImageResource(R.drawable.ic_launcher_background)
                }

                followers.text = "Followers: " + user.followers.toString()
            }
        }

        val userWorkoutLiveData = userRepository.getUserWithSimpleWorkouts(userId)
        userWorkoutLiveData.observe(requireContext() as LifecycleOwner){
            userWorkout->
            if(userWorkout != null){
                workoutcount.text = "Workouts: " + userWorkout.workouts.size.toString()
                if(userWorkout.workouts.isNotEmpty()){
                    workoutlayout.visibility = View.VISIBLE

                    //Get Last Workout
                    val workout = userWorkout.workouts[userWorkout.workouts.size-1]

                    //Duration and Likes
                    workouttime.text = (workout.duration.toFloat()/60).toString() + " hrs"
                    workoutlikes.text = workout.likes.toString() + " likes"

                    //Last Comment
                    if(workout.comments.isNotEmpty()){
                        val getComment = workout.comments[workout.comments.size-1]
                        if(getComment.imageData.size == 0){
                            commentImg.setImageResource(R.drawable.ic_launcher_background)
                        }
                        else{
                            commentImg.setImageBitmap(PhotoUtil.byteArrayToBitmap(getComment.imageData))
                        }
                        comment.text = getComment.comment
                    }
                    recordslive = workoutRepository.getWorkoutWithExercisesAndSets(listOf(workout.workoutId!!))

                }
                else{
                    workoutlayout.visibility = View.GONE
                }
            }
        }

        val userFollowersLiveData = userRepository.getUserWithFollowing(userId)
        userFollowersLiveData.observe(requireContext() as LifecycleOwner){
            userFollowers->
            if(userFollowers != null){
                following.text = "Following: " + userFollowers.followings.size.toString()
            }
        }
    }

    private fun getDurationBarData(view : View){
        val userWorkoutLiveData = userRepository.getUserWithSimpleWorkouts(userId)
        userWorkoutLiveData.observe(requireContext() as LifecycleOwner){
            userWorkout->

            //Reset all stats back to 0
            for(i in 0 .. 6){
                durationStats[i] = 0
            }
            if(userWorkout != null){
                val startdate = getCalendarInstance()
                val enddate = getCalendarInstance()
                enddate.add(Calendar.DAY_OF_MONTH, 1)

                var hoursthisweek = 0
                //Loop through
                for(i in 0 .. 6){
                    for(workout in userWorkout.workouts){
                        if(workout.timestamp in startdate.timeInMillis .. enddate.timeInMillis){
                            durationStats[6-i] += workout.duration
                        }
                    }
                    hoursthisweek += durationStats[6-i]
                    startdate.add(Calendar.DAY_OF_MONTH, -1)
                    enddate.add(Calendar.DAY_OF_MONTH, -1)
                }
                val hrsthisweek = view.findViewById<TextView>(R.id.stats_title)
                hrsthisweek.text = (hoursthisweek/60).toString() + " hours this week"
            }
        }
    }

    private fun getCalendarInstance() : Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND,0)
        return calendar
    }

    override fun onResume() {
        super.onResume()
        println("Resume")
        recordslive.observe(viewLifecycleOwner) { records ->
            recordslive.removeObservers(viewLifecycleOwner)
            if (records != null) {
                val workoutrecord = helperMapWorkoutRecords(records)
                if (workoutrecord.isNotEmpty()) {
                    volumeTxt.text =workoutrecord[0].volume.toString() + "lbs"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("Destroy")
        recordslive.removeObservers(viewLifecycleOwner)
    }
}
