package com.example.fit_connect.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val SET_TABLE_NAME = "set"
const val SET_ID_NAME = "set_id"

@Entity(
    tableName = SET_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = [EXERCISE_ID_NAME],
            childColumns = [EXERCISE_ID_NAME]
        )
    ]
)
data class ExerciseSet(
    val volume: Int,
    val reps: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SET_ID_NAME)
    val setId: Long? = null,
    @ColumnInfo(name = EXERCISE_ID_NAME)
    val exerciseId: Long
)