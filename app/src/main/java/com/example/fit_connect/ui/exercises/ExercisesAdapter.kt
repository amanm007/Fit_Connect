package com.example.fit_connect.ui.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R
import com.example.fit_connect.data.workout.ExerciseType

class ExercisesAdapter(
    private val exercises: List<ExerciseType>,
    private val onItemClicked: (ExerciseType) -> Unit
) : RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val exerciseImage: ImageView = view.findViewById(R.id.exercise_image)
        val exerciseName: TextView = view.findViewById(R.id.exercise_name)
        val exerciseCategory: TextView = view.findViewById(R.id.exercise_category)
    }

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.type.displayName
        holder.exerciseCategory.text = exercise.type.category

        if(exercise.type.id in 0 .. 7){
            holder.exerciseImage.setImageResource(exerciseImages[exercise.type.id])
        }
        else {
            holder.exerciseImage.setImageResource(R.drawable.ic_launcher_background) // Replace with actual image
        }
        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClicked(exercise)
        }
    }

    override fun getItemCount() = exercises.size
}

