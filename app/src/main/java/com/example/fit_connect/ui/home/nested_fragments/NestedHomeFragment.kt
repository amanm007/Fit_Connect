package com.example.fit_connect.ui.home.nested_fragments
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.LegendLayout
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.UserDao
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.data.workout.Exercise
import com.example.fit_connect.data.workout.Workout
import com.example.fit_connect.data.workout.WorkoutDao
import com.example.fit_connect.data.workout.WorkoutRepository
import com.example.fit_connect.databinding.FragmentNestedHomeBinding
import com.example.fit_connect.ui.UserActivity
import com.example.fit_connect.ui.home.nested_fragments.listadapters.PRListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.AxisValue
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.view.LineChartView
import java.util.Arrays
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


class NestedHomeFragment: Fragment() {

    private var _binding: FragmentNestedHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var database : FitConnectDatabase
    private lateinit var databasedao : UserDao
    private lateinit var userRepository: UserRepository

    private lateinit var workoutdao : WorkoutDao
    private lateinit var workoutRepository: WorkoutRepository

    private var userWorkoutDuration : MutableList<Int> = mutableListOf(0,0,0,0,0,0,0)
    private var followerWorkoutDuration1 : MutableList<Int> = mutableListOf(0,0,0,0,0,0,0)
    private var followerWorkoutDuration2 : MutableList<Int> = mutableListOf(0,0,0,0,0,0,0)
    private var follower_count = 0
    private var follower1_name = ""
    private var follower2_name = ""


    private val user_id = "USER_ID"
    private var userId : Long = 0

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
    //PR History (Only 3 most recent)
    private var exerciseHistoryList : MutableList<Exercise>  = mutableListOf()
    private lateinit var arrayAdapter : PRListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        _binding = FragmentNestedHomeBinding.inflate(inflater, container, false)
        val root :  View = binding.root

        val bundle = (activity as? UserActivity)?.sharedBundle
        userId = bundle!!.getLong(user_id)


        database = FitConnectDatabase.getInstance(requireContext())
        databasedao = database.userDao()
        userRepository = UserRepository(databasedao)
        workoutdao = database.workoutDao()
        workoutRepository = WorkoutRepository(workoutdao)


        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -10)

        // Initialize values for userId 1 for testing
        /*
        for(i in 1 .. 11){

            val workout = Workout(userId = 1, duration = 30 * i, visible = true, timestamp = calendar.timeInMillis)
            calendar.add(Calendar.DAY_OF_MONTH,1)
            CoroutineScope(IO).launch{
                workoutRepository.insertWorkout(workout)
            }

        }
        */
        calculateWeeklyStreak(root)
        setFitGroupTrack(root)
        setPRHistory(root)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun calculateWeeklyStreak(root: View){
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val weekly_fire_layout : LinearLayout = root.findViewById(R.id.weekly_fire_layout)
        val daily_fire_layout : LinearLayout = root.findViewById(R.id.daily_fire_layout)
        val weekly_fire_text : TextView = root.findViewById(R.id.weekly_fire_txt)
        val daily_fire_text : TextView = root.findViewById(R.id.daily_fire_txt)
        val weekly_streak_text : TextView = root.findViewById(R.id.weekly_streak_des_txt)


        val endDate = Calendar.getInstance()
        endDate.set(Calendar.HOUR_OF_DAY, 0)
        endDate.set(Calendar.MINUTE,0)
        endDate.set(Calendar.SECOND,0)
        endDate.set(Calendar.MILLISECOND, 0)
        endDate.add(Calendar.DAY_OF_MONTH, 1)

        val startDate = Calendar.getInstance()
        startDate.set(Calendar.HOUR_OF_DAY,0)
        startDate.set(Calendar.HOUR_OF_DAY, 0)
        startDate.set(Calendar.MINUTE,0)
        startDate.set(Calendar.SECOND,0)
        startDate.set(Calendar.MILLISECOND, 0)


        //Retrieve workout list and calculate streak
        val userWorkouts = userRepository.getUserWithSimpleWorkouts(userId)
        userWorkouts.observe(requireContext() as LifecycleOwner) { userWorkoutData ->
            val workoutList = userWorkoutData.workouts


            var streak_count = 0

            //Check if today workout has been recorded
            for (workouts in workoutList) {
                if (workouts.timestamp in startDate.timeInMillis..endDate.timeInMillis) {
                    weekly_streak_text.text =
                        getString(R.string.good_work_track_your_efforts_tomorrow_to_keep_the_streak)
                    streak_count++
                    break

                }
            }

            var isStreakContinue = true
            var isWorkoutInDateRange = false
            //Calculate Remaining Streak. Stop once a day has been missed
            while(isStreakContinue){

                startDate.add(Calendar.DAY_OF_MONTH, -1)
                endDate.add(Calendar.DAY_OF_MONTH, -1)

                isWorkoutInDateRange = false
                for(workouts in workoutList){
                    if(workouts.timestamp in startDate.timeInMillis.. endDate.timeInMillis
                        && !isWorkoutInDateRange){
                        streak_count++
                        isWorkoutInDateRange = true
                    }
                }
                if(!isWorkoutInDateRange){
                    isStreakContinue = false
                }
            }

            //Add Fire Depending on streak days
            val weeks : Int = streak_count / 7
            val days : Int = streak_count % 7

            for (i in 1..weeks){
                val fire_imageView = ImageView(getActivity())
                fire_imageView.setImageResource(R.drawable.ic_fire_streak)

                val layoutParams = LinearLayout.LayoutParams(100,100)
                fire_imageView.layoutParams = layoutParams
                weekly_fire_layout.addView(fire_imageView)
            }

            if(weeks != 0) weekly_fire_text.text = weeks.toString() + " weeks"

            for (i in 1 .. days){
                val fire_imageView = ImageView(getActivity())
                fire_imageView.setImageResource(R.drawable.ic_fire_streak)

                val layoutParams = LinearLayout.LayoutParams(75,75)
                fire_imageView.layoutParams = layoutParams
                daily_fire_layout.addView(fire_imageView)
            }
            if(days != 0) daily_fire_text.text = days.toString() + " days"

        }

    }

    private fun setFitGroupTrack(root : View){

        val followingLiveData = userRepository.getUserWithFollowing(userId)
        //Retrieve Follower data

        followingLiveData.observe(requireContext() as LifecycleOwner){

            userandfollowers ->
            val followers = userandfollowers.followings
            var follower1Index = -1
            var follower2Index = -1
            if(followers.size == 0){
                follower_count = 0
            }
            else if(followers.size == 1){

                follower1_name = followers[0].friendName
                getDurationArray(followers[0].friendId,1,root)
                follower_count = 1
            }

            else if(followers.size == 2){

                follower1_name = followers[0].friendName
                follower2_name = followers[1].friendName

                getDurationArray(followers[0].friendId,1,root)
                getDurationArray(followers[1].friendId,2,root)
                follower_count = 2
            }

            else{
                follower1Index = kotlin.random.Random.nextInt(0,followers.size)
                follower2Index = follower1Index
                while(follower1Index == follower2Index)
                    follower2Index = kotlin.random.Random.nextInt(0, followers.size)

                follower1_name = followers[follower1Index].friendName
                follower2_name = followers[follower1Index].friendName

                getDurationArray(followers[follower1Index].friendId,1,root)
                getDurationArray(followers[follower2Index].friendId,2,root)
                follower_count = 2

            }

        }
        getDurationArray(userId, 0, root)

    }

    //Get Duration Array
    //Type: 0 = User, 1 = Follower1, 2 = Follower 2
    private fun getDurationArray(Id: Long, type : Int, root:View){

        val endDate = Calendar.getInstance()
        println(userId)
        endDate.set(Calendar.HOUR_OF_DAY, 0)
        endDate.set(Calendar.MINUTE,0)
        endDate.set(Calendar.SECOND,0)
        endDate.set(Calendar.MILLISECOND, 0)
        endDate.add(Calendar.DAY_OF_MONTH, 1)

        val startDate = Calendar.getInstance()
        startDate.set(Calendar.HOUR_OF_DAY,0)
        startDate.set(Calendar.HOUR_OF_DAY, 0)
        startDate.set(Calendar.MINUTE,0)
        startDate.set(Calendar.SECOND,0)
        startDate.set(Calendar.MILLISECOND, 0)

        //Reset User/Follower work values to 0
        for(i in 0 .. 6){
            if(type == 0){
                userWorkoutDuration[i] = 0
            }
            else if(type == 1){
                followerWorkoutDuration1[i] = 0
            }
            else{
                followerWorkoutDuration2[i] = 0
            }
        }


        //Get a Follower's Workout and get total work values of the week
        val userLiveWorkout = userRepository.getUserWithSimpleWorkouts(Id)
        userLiveWorkout.observe(requireContext() as LifecycleOwner){
            userwithsimpleworkout ->
            val workouts = userwithsimpleworkout.workouts
            for(i in 0 .. 6){
                for(workout in workouts){
                    if(workout.timestamp in startDate.timeInMillis .. endDate.timeInMillis){
                        if(type == 0){
                            userWorkoutDuration[6-i] += workout.duration
                        }
                        else if(type == 1){
                            followerWorkoutDuration1[6-i] += workout.duration
                        }
                        else{
                            followerWorkoutDuration2[6-i] += workout.duration
                        }

                    }
                }
                startDate.add(Calendar.DAY_OF_MONTH, -1)
                endDate.add(Calendar.DAY_OF_MONTH, -1)
            }

            initChart(root)
        }


    }
    private fun initChart(root:View){
        val chartView = root.findViewById<LineChartView>(R.id.activity_chart_view)

        val values1 = mutableListOf(
             PointValue(0f,userWorkoutDuration[0].toFloat() / 60),
            PointValue(1f,userWorkoutDuration[1].toFloat() / 60),
            PointValue(2f,userWorkoutDuration[2].toFloat()/60),
            PointValue(3f,userWorkoutDuration[3].toFloat()/60),
            PointValue(4f,userWorkoutDuration[4].toFloat()/60),
            PointValue(5f,userWorkoutDuration[5].toFloat()/60),
            PointValue(6f,userWorkoutDuration[6].toFloat()/60)
        )
        val values2 = mutableListOf(
            PointValue(0f,followerWorkoutDuration1[0].toFloat()/60),
            PointValue(1f,followerWorkoutDuration1[1].toFloat()/60),
            PointValue(2f,followerWorkoutDuration1[2].toFloat()/60),
            PointValue(3f,followerWorkoutDuration1[3].toFloat()/60),
            PointValue(4f,followerWorkoutDuration1[4].toFloat()/60),
            PointValue(5f,followerWorkoutDuration1[5].toFloat()/60),
            PointValue(6f,followerWorkoutDuration1[6].toFloat()/60)
        )
        val values3 = mutableListOf(
            PointValue(0f,followerWorkoutDuration2[0].toFloat() / 60),
            PointValue(1f,followerWorkoutDuration2[1].toFloat() / 60),
            PointValue(2f,followerWorkoutDuration2[2].toFloat()/60),
            PointValue(3f,followerWorkoutDuration2[3].toFloat()/60),
            PointValue(4f,followerWorkoutDuration2[4].toFloat()/60),
            PointValue(5f,followerWorkoutDuration2[5].toFloat()/60),
            PointValue(6f,followerWorkoutDuration2[6].toFloat()/60)
        )

        //Make Lines
        val Line = Line(values1).apply{
            color = Color.BLUE
            setHasLines(true)
            setHasPoints(true)

        }
        val Line2 = Line(values2).apply{
            color = Color.RED
            setHasLines(true)
            setHasPoints(true)
        }
        val Line3 = Line(values3).apply{
            color = Color.GREEN
            setHasLines(true)
            setHasPoints(true)
        }

        //Attach Lines and Show Legend
        val friendLegend1 : TextView = root.findViewById(R.id.friend1_legend_txt)
        val friendLegend2 : TextView = root.findViewById(R.id.friend2_legend_txt)

        val data = LineChartData()
        if(follower_count == 0){
            data.lines = listOf(Line)
        }
        if(follower_count == 1){
            data.lines = listOf(Line, Line2)
            friendLegend1.text = follower1_name
        }
        if(follower_count == 2){
            data.lines = listOf(Line, Line2, Line3)
            friendLegend1.text = follower1_name
            friendLegend2.text = follower2_name
        }

        //Axis
        val axisX = Axis().apply {
            name = "Weekdays"
            textColor = Color.WHITE
            textSize = 6
        }
        val axisValues = mutableListOf<AxisValue>()
        axisValues.add(AxisValue(0f).setLabel(weekdays[start_dayofWeekIndex]))
        axisValues.add(AxisValue(1f).setLabel(weekdays[(start_dayofWeekIndex+1)%7]))
        axisValues.add(AxisValue(2f).setLabel(weekdays[(start_dayofWeekIndex+2)%7]))
        axisValues.add(AxisValue(3f).setLabel(weekdays[(start_dayofWeekIndex+3)%7]))
        axisValues.add(AxisValue(4f).setLabel(weekdays[(start_dayofWeekIndex+4)%7]))
        axisValues.add(AxisValue(5f).setLabel(weekdays[(start_dayofWeekIndex+5)%7]))
        axisValues.add(AxisValue(6f).setLabel(weekdays[(start_dayofWeekIndex+6)%7]))

        axisX.values = axisValues

        val axisY = Axis().apply {
            name = "Hours"
            textColor = Color.WHITE
            textSize = 10
        }

        data.axisXBottom = axisX
        data.axisYLeft = axisY
        // Set data to chart
        chartView.lineChartData = data

    }

    private fun setPRHistory(root : View){
        val prListView = root.findViewById<ListView>(R.id.pr_recent_list)

        arrayAdapter = PRListAdapter(requireContext(), exerciseHistoryList)
        prListView.adapter = arrayAdapter

        //Observe (Yes its nested. It looks gross but i need to get all exercises)
        //Only get 3 PR's
        val liveWorkoutList = userRepository.getUserWithSimpleWorkouts(userId)
        liveWorkoutList.observe(requireContext() as LifecycleOwner) {
            workoutlist ->
            exerciseHistoryList = mutableListOf()
            var atmost3 = 0

            if (workoutlist != null) {
                for (workout in workoutlist.workouts) {
                    val liveExerciseList = workoutRepository.getWorkoutWithExercises(workout.workoutId!!)
                    liveExerciseList.observe(requireContext() as LifecycleOwner){
                            exerciselist ->
                        if(exerciselist != null){
                            for(exercise in exerciselist.exercises){
                                if(atmost3 == 3){
                                    break
                                }
                                exerciseHistoryList.add(exercise)
                                atmost3++

                            }
                            arrayAdapter.replace(exerciseHistoryList)
                            arrayAdapter.notifyDataSetChanged()
                        }
                    }
                    if(atmost3 == 3){
                        break
                    }
                }
            }

        }
        /*
        val arrayList : ArrayList<Int> = java.util.ArrayList(3)
        arrayList.add(1)
        arrayList.add(2)
        arrayList.add(3)

        val arrayAdapter = PRListAdapter(requireActivity(), arrayList)
        val prListView : ListView = root.findViewById(R.id.pr_recent_list)
        prListView.adapter = arrayAdapter

         */
    }
}