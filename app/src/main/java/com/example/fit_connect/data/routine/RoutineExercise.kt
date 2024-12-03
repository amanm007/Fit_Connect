package com.example.fit_connect.data.routine

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.fit_connect.data.workout.EXERCISE_ID_NAME
import com.example.fit_connect.data.workout.EXERCISE_TYPE_ID_NAME
import com.example.fit_connect.data.workout.ExerciseType

const val ROUTINE_EXERCISE_TABLE_NAME = "routine_exercise"

@Entity(
    tableName = ROUTINE_EXERCISE_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = ExerciseType::class,
            parentColumns = [EXERCISE_TYPE_ID_NAME],
            childColumns = [EXERCISE_TYPE_ID_NAME]
        ),
        ForeignKey(
            entity = RoutineWorkout::class,
            parentColumns = [WORKOUT_ID_NAME],
            childColumns = [WORKOUT_ID_NAME]
        )
    ]
)
data class RoutineExercise(
    val restTimer: Int,
    val notes: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = EXERCISE_ID_NAME)
    val exerciseId: Long? = null,
    @ColumnInfo(name = EXERCISE_TYPE_ID_NAME)
    val exerciseTypeId: Long,
    @ColumnInfo(name = WORKOUT_ID_NAME)
    val workoutId: Long
)