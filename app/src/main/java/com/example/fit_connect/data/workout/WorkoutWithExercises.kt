package com.example.fit_connect.data.workout

import androidx.room.Embedded
import androidx.room.Relation

class WorkoutWithExercises(
    @Embedded
    val workoutToExercise: WorkoutToExercise? = null,

    @Relation(
        parentColumn = WORKOUT_ID_NAME,
        entityColumn = WORKOUT_ID_NAME
    )
    val workout: Workout,

    @Relation(
        parentColumn = EXERCISE_ID_NAME,
        entityColumn = EXERCISE_ID_NAME
    )
    val exercises: List<Exercise> = listOf()
)
