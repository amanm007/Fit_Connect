package com.example.fit_connect.data.workout

import androidx.room.Embedded
import androidx.room.Relation

class WorkoutWithExercises(
    @Embedded
    val workout: Workout? = null,

    @Relation(
        parentColumn = WORKOUT_ID_NAME,
        entityColumn = WORKOUT_ID_NAME
    )
    val exercises: List<Exercise> = listOf()
)
