package com.example.fit_connect.data.user

import androidx.room.Embedded
import androidx.room.Relation
import com.example.fit_connect.data.workout.Workout

class UserWithSimpleWorkout(
    @Embedded
    val user: User? = null,

    @Relation(
        parentColumn = USER_ID_NAME,
        entityColumn = USER_ID_NAME
    )
    val workouts: List<Workout> = listOf()
)