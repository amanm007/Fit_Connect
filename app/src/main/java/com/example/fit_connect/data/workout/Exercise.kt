package com.example.fit_connect.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val EXERCISE_TABLE_NAME = "exercise"
const val EXERCISE_ID_NAME = "exercise_id"

@Entity(tableName = EXERCISE_TABLE_NAME)
class Exercise(
    val restTimer: Int,
    val notes: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = EXERCISE_ID_NAME)
    val exerciseId: Long? = null,
    @ColumnInfo(name = EXERCISE_TYPE_ID_NAME)
    val exerciseTypeId: Long
)