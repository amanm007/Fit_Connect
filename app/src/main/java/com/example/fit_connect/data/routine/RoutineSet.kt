package com.example.fit_connect.data.routine

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.fit_connect.data.workout.EXERCISE_ID_NAME

const val ROUTINE_SET_TABLE_NAME = "routine_set"
const val SET_ID_NAME = "set_id"

@Entity(
    tableName = ROUTINE_SET_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = RoutineExercise::class,
            parentColumns = [EXERCISE_ID_NAME],
            childColumns = [EXERCISE_ID_NAME]
        )
    ]
)
data class RoutineExerciseSet(
    val volume: Int,
    val reps: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SET_ID_NAME)
    val setId: Long? = null,
    @ColumnInfo(name = EXERCISE_ID_NAME)
    val exerciseId: Long
)