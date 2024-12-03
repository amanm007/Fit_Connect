package com.example.fit_connect.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val FOLLOWING_TABLE_NAME = "following"
const val FOLLOWING_ID_NAME = "following_id"
const val FRIEND_ID_NAME = "friend_id"

@Entity(
    tableName = FOLLOWING_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = [USER_ID_NAME],
            childColumns = [USER_ID_NAME]
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = [USER_ID_NAME],
            childColumns = [FRIEND_ID_NAME]
        )
    ]
)
class Following(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = FOLLOWING_ID_NAME)
    val followingId: Long? = null,
    @ColumnInfo(name = USER_ID_NAME)
    val userId: Long,
    @ColumnInfo(name = FRIEND_ID_NAME)
    val friendId: Long,
    val friendName : String,
)