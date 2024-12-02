package com.example.fit_connect.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val EXERCISE_TABLE_NAME = "exercise"
const val EXERCISE_ID_NAME = "exercise_id"

@Entity(
    tableName = EXERCISE_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = ExerciseType::class,
            parentColumns = [EXERCISE_TYPE_ID_NAME],
            childColumns = [EXERCISE_TYPE_ID_NAME]
        ),
        ForeignKey(
            entity = Workout::class,
            parentColumns = [WORKOUT_ID_NAME],
            childColumns = [WORKOUT_ID_NAME]
        )
    ]
)
data class Exercise(
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