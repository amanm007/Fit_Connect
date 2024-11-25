package com.example.fit_connect.data.user

import androidx.room.Embedded
import androidx.room.Relation

class UserAndFollowers(
    @Embedded
    val user: User? = null,

    @Relation(
        parentColumn = USER_ID_NAME,
        entityColumn = USER_ID_NAME,
    )
    val followings: List<Following>
)