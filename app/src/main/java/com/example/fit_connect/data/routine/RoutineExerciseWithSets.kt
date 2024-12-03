package com.example.fit_connect.data.routine

import androidx.room.Embedded
import androidx.room.Relation
import com.example.fit_connect.data.workout.EXERCISE_ID_NAME
import com.example.fit_connect.data.workout.EXERCISE_TYPE_ID_NAME
import com.example.fit_connect.data.workout.ExerciseSet
import com.example.fit_connect.data.workout.ExerciseType

class RoutineExerciseWithSets(
    @Embedded
    val exercise: RoutineExercise? = null,

    @Relation(
        parentColumn = EXERCISE_ID_NAME,
        entityColumn = EXERCISE_ID_NAME
    )
    val sets: List<ExerciseSet>,

    @Relation(
        parentColumn = EXERCISE_TYPE_ID_NAME,
        entityColumn = EXERCISE_TYPE_ID_NAME
    )
    val exerciseType: ExerciseType
)