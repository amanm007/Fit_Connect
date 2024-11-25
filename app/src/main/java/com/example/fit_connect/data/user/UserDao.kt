package com.example.fit_connect.data.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Insert
    suspend fun insertFollowing(following: Following): Long

    @Query("SELECT * FROM $USER_TABLE_NAME")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM $USER_TABLE_NAME WHERE $USER_ID_NAME = :userId")
    fun getUser(userId: Long): LiveData<User?>

    @Transaction
    @Query("SELECT * FROM $USER_TABLE_NAME WHERE $USER_ID_NAME = :userId")
    fun getUserWithSimpleWorkouts(userId: Long): LiveData<UserWithSimpleWorkout>

    @Transaction
    @Query("SELECT * FROM $USER_TABLE_NAME WHERE $USER_ID_NAME = :userId")
    fun getUserWithFollowing(userId: Long): LiveData<UserAndFollowers>
}