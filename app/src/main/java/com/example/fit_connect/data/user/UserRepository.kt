package com.example.fit_connect.data.user

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User): Long
        = withContext(IO) { userDao.insertUser(user) }

    suspend fun insertFollowing(following: Following): Long
        = withContext(IO) { userDao.insertFollowing(following) }

    fun getAllUsers() = userDao.getAllUsers()

    fun getUser(userId: Long) = userDao.getUser(userId)

    fun getUserWithSimpleWorkouts(userId: Long) = userDao.getUserWithSimpleWorkouts(userId)

    fun getUserWithFollowing(userId: Long) = userDao.getUserWithFollowing(userId)
}