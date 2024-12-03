package com.example.fit_connect

data class WorkoutRecord(
    val date: Long,     // timestamp
    val duration: Int,  // Duration in minutes
    val volume: Int,    // Volume in lbs/kgs = weight * reps for each set
    val reps: Int       // Repetitions = reps added up across all exercises in a workout
)
