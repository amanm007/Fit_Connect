package com.example.fit_connect.data.workout

import androidx.room.Embedded
import androidx.room.Relation

class ExerciseWithSets(
    @Embedded
    val exercise: Exercise? = null,

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