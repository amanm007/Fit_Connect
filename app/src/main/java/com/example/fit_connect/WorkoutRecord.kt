package com.example.fit_connect

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_records")
data class WorkoutRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,  // Store as a timestamp
    val duration: Int,  // Duration in minutes
    val volume: Int,    // Volume in lbs/kgs
    val reps: Int       // Repetitions
)
