package com.example.fit_connect.data.workout

import androidx.room.Embedded
import androidx.room.Relation

class ExerciseWithSets(
    @Embedded
    val exerciseWithSets: ExerciseWithSets? = null,

    @Relation(
        parentColumn = EXERCISE_ID_NAME,
        entityColumn = EXERCISE_ID_NAME
    )
    val exercise: Exercise,

    @Relation(
        parentColumn = SET_ID_NAME,
        entityColumn = SET_ID_NAME
    )
    val sets: List<ExerciseSet>
)