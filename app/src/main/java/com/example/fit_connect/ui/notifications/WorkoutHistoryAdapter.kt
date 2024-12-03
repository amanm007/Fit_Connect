package com.example.fit_connect.ui.notifications

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R
import java.util.Locale

class WorkoutHistoryAdapter(
    private val workoutHistories: List<WorkoutHistory>
) : RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {
    class WorkoutHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.workout_time)
        val duration: TextView = view.findViewById(R.id.workout_duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_workout_history, parent, false)
        return WorkoutHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        val workoutHistory = workoutHistories[position]
        val timeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        holder.time.text = timeFormatter.format(workoutHistory.time)
        holder.duration.text = workoutHistory.duration.toString()
    }

    override fun getItemCount() = workoutHistories.size
}