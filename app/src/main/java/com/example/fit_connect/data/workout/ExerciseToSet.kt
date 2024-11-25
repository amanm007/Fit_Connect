package com.example.fit_connect.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val EXERCISE_TO_SET_TABLE_NAME = "exercise_to_set"
const val EXERCISE_TO_SET_ID_NAME = "exercise_to_sets_id"

@Entity(
    tableName = EXERCISE_TO_SET_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = [EXERCISE_ID_NAME],
            childColumns = [EXERCISE_ID_NAME]
        ),
        ForeignKey(
            entity = ExerciseSet::class,
            parentColumns = [SET_ID_NAME],
            childColumns = [SET_ID_NAME]
        )
    ]
)
class ExerciseToSet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = EXERCISE_TO_SET_ID_NAME)
    val exerciseToSetId: Long? = null,
    @ColumnInfo(name = EXERCISE_ID_NAME)
    val exerciseId: Long,
    @ColumnInfo(name = SET_ID_NAME)
    val setId: Long
)