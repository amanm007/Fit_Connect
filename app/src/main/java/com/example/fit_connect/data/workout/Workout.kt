package com.example.fit_connect.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.fit_connect.data.user.USER_ID_NAME
import com.example.fit_connect.data.user.User

const val WORKOUT_TABLE_NAME = "workout"
const val WORKOUT_ID_NAME = "workout_id"

@Entity(
    tableName = WORKOUT_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = [USER_ID_NAME],
            childColumns = [USER_ID_NAME]
        )
    ]
)
data class Workout(
    val timestamp: Long,
    val duration: Int,
    val visible: Boolean,

    var likes : Int = 0,
    var comments : MutableList<Comments> = mutableListOf(),
    var likedList : MutableList<Long> = mutableListOf(),

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = WORKOUT_ID_NAME)
    val workoutId: Long? = null,
    @ColumnInfo(name = USER_ID_NAME)
    val userId: Long
)