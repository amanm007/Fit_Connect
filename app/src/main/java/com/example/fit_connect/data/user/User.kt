package com.example.fit_connect.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val USER_TABLE_NAME = "user"
const val USER_ID_NAME = "user_id"

@Entity(tableName = USER_TABLE_NAME)
data class User(
    val firstName: String,
    val lastName: String,
    val email: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = USER_ID_NAME)
    val userId: Long? = null
)