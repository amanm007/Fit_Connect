package com.example.fit_connect.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

const val USER_TABLE_NAME = "user"
const val USER_ID_NAME = "user_id"
const val USER_USERNAME = "username"

@Entity(tableName = USER_TABLE_NAME, indices = [Index(value = ["username"], unique = true)])
data class User(
    val firstName: String,
    val lastName: String,

    @ColumnInfo(name = USER_USERNAME)
    var userName:String = "", //UserName of account stored as a string

    var email: String, //Email stored as a string
    var password : String = "", //Password Stored as a string
    var laston : Long = 0, //Displays when the user was last online stored in long as a timestamp
    var imageData : ByteArray? = null,
    var followers : Int = 0,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = USER_ID_NAME)
    val userId: Long? = null
)