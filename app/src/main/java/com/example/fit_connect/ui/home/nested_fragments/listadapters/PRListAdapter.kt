package com.example.fit_connect.ui.home.nested_fragments.listadapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.workout.Exercise
import com.example.fit_connect.data.workout.ExerciseTypeEnum
import com.example.fit_connect.data.workout.WorkoutRepository

class PRListAdapter (private val context: Context, private var arrayList:MutableList<Exercise>) : BaseAdapter(){
    private val database = FitConnectDatabase.getInstance(context)
    private val workoutDao = database.workoutDao()
    private val workoutRepository = WorkoutRepository(workoutDao)

    val exerciseImages = listOf(
        R.drawable.bench_press,
        R.drawable.squat,
        R.drawable.deadlift,
        R.drawable.overheadpress,
        R.drawable.pullups,
        R.drawable.pushups,
        R.drawable.barbellrow,
        R.drawable.dumbellcurl
    )

    override fun getCount(): Int{
        return arrayList.size
    }
    override fun getItem(position: Int): Exercise{
        return arrayList[position]
    }
    override fun getItemId(position: Int) : Long{
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val customView: View = View.inflate(context, R.layout.pr_listadapter, null)

        //Observe Live Data
        val imgView : ImageView = customView.findViewById(R.id.exercise_imgview)
        val exercise_routine : TextView = customView.findViewById(R.id.exercise_routine_txt)
        val exercise_part : TextView = customView.findViewById(R.id.exercise_routine_part_txt)

        val exerciseTypeLiveData = workoutRepository.getExerciseType(arrayList[position].exerciseTypeId)
        exerciseTypeLiveData.observe(context as LifecycleOwner){
            exerciseType ->
            if(exerciseType != null){
                imgView.setImageResource(exerciseImages[exerciseType.type.id])
                exercise_routine.text = exerciseType.type.displayName
                exercise_part.text = exerciseType.type.category
            }
        }
        return customView
    }

    fun replace(newExercises : MutableList<Exercise>){
        arrayList = newExercises
    }

}