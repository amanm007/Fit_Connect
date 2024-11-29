package com.example.fit_connect.ui.exercises

data class WorkoutSet(
    val setNumber: Int,
    val weight: Float,
    val reps: Int,
    var isCompleted: Boolean = false
)