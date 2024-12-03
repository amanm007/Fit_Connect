package com.example.fit_connect.ui.home.nested_fragments.listadapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.data.workout.Exercise
import com.example.fit_connect.data.workout.WorkoutRepository

class WorkoutListAdapter(private val context: Context, private var arrayList:MutableList<Exercise>) : BaseAdapter(){
    private var volume = 0
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
    private val database = FitConnectDatabase.getInstance(context)
    private val workutDatabaseDao = database.workoutDao()
    private val workoutRepository = WorkoutRepository(workutDatabaseDao)

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
        val customView: View = View.inflate(context, R.layout.following_workout_listadapter, null)

        val workoutImg : ImageView = customView.findViewById(R.id.following_workout_img)
        val workoutDescriptionTxt : TextView = customView.findViewById(R.id.following_workout_description_txt)

        var workoutstring = ""

        val exercisetoExerciseTypeLiveData = workoutRepository.getExerciseType(arrayList[position].exerciseTypeId)
        exercisetoExerciseTypeLiveData.observe(context as LifecycleOwner){
            exercisetoExerciseType->
            if(exercisetoExerciseType != null){
                workoutImg.setImageResource(exerciseImages[exercisetoExerciseType.type.id])
                workoutstring = exercisetoExerciseType.type.displayName
            }
        }
        val exercisetoSetsLiveData = workoutRepository.getExerciseWithSets(arrayList[position].exerciseId!!)
        var set_count = 0
        exercisetoSetsLiveData.observe(context as LifecycleOwner){
            exercisetoSets ->
            if(exercisetoSets != null){
                for(sets in exercisetoSets.sets){
                    volume += sets.volume
                    set_count++
                }
                workoutDescriptionTxt.text = set_count.toString() + " sets of " + workoutstring
            }
        }
        return customView
    }
    fun replace(newExercises : MutableList<Exercise>){
        arrayList = newExercises
        volume = 0

    }
    fun getVolume() : Int{
        return volume
    }
}