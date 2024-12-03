package com.example.fit_connect.data.routine

import androidx.room.Embedded
import androidx.room.Relation

class RoutineWorkoutWithExercises(
    @Embedded
    val workout: RoutineWorkout? = null,

    @Relation(
        parentColumn = WORKOUT_ID_NAME,
        entityColumn = WORKOUT_ID_NAME
    )
    val exercises: List<RoutineExercise> = listOf()
)
