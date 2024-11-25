package com.example.fit_connect.ui.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R

data class Exercise(val name: String, val category: String)

class ExercisesAdapter(private val exercises: List<Exercise>) :
    RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val exerciseImage: ImageView = view.findViewById(R.id.exercise_image)
        val exerciseName: TextView = view.findViewById(R.id.exercise_name)
        val exerciseCategory: TextView = view.findViewById(R.id.exercise_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.name
        holder.exerciseCategory.text = exercise.category
        holder.exerciseImage.setImageResource(R.drawable.ic_launcher_background) // Replace with actual image
    }

    override fun getItemCount() = exercises.size
}
