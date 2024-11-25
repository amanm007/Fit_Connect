package com.example.fit_connect.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val WORKOUT_TO_EXERCISE_TABLE_NAME = "workout_to_exercise"
const val WORKOUT_TO_EXERCISE_ID_NAME = "workout_to_exercise_id"

@Entity(
    tableName = WORKOUT_TO_EXERCISE_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = [WORKOUT_ID_NAME],
            childColumns = [WORKOUT_ID_NAME]
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = [EXERCISE_ID_NAME],
            childColumns = [EXERCISE_ID_NAME]
        )
    ]
)
class WorkoutToExercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = WORKOUT_TO_EXERCISE_ID_NAME)
    val workoutToExerciseId: Long? = null,
    @ColumnInfo(name = WORKOUT_ID_NAME)
    val workoutId: Long,
    @ColumnInfo(name = EXERCISE_ID_NAME)
    val exerciseId: Long
)