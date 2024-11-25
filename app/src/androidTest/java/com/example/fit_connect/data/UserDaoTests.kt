package com.example.fit_connect.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.fit_connect.awaitValue
import com.example.fit_connect.data.user.UserDao
import kotlinx.coroutines.runBlocking
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fit_connect.TestUtil
import com.example.fit_connect.data.SeedData.Companion.makeTestUser
import com.example.fit_connect.data.SeedData.Companion.makeTestWorkout
import com.example.fit_connect.data.workout.WorkoutDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTests {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: FitConnectDatabase
    private lateinit var userDao: UserDao
    private lateinit var workoutDao: WorkoutDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = TestUtil.createTestDatabase(context)
        userDao = db.userDao()
        workoutDao = db.workoutDao()
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun testGetEmptyUsers() = runBlocking {
        val users = userDao.getAllUsers().awaitValue()
        assert(users.isEmpty())
    }

    @Test
    fun testGetAllUsers() = runBlocking {
        val expectedUsers = SeedData.testUsers.map {
            val uid = userDao.insertUser(it)
            makeTestUser(uid)
        }.sortedBy { it.userId }

        val users = userDao.getAllUsers().awaitValue().sortedBy { it.userId }
        assert(users == expectedUsers)
    }

    @Test
    fun testGetUserById() = runBlocking {
        val expectedUser = SeedData.testUsers.map {
            val uid = userDao.insertUser(it)
            makeTestUser(uid)
        }.first()

        val user = userDao.getUser(expectedUser.userId!!).awaitValue()
        assert(user != null)
        assert(user == expectedUser)
    }

    @Test
    fun testInsertUser() = runBlocking {
        val expectedUser = SeedData.testUsers.first()
        userDao.insertUser(expectedUser)

        val users = userDao.getAllUsers().awaitValue()
        assert(users.size == 1)
        assert(users.first() == expectedUser)
    }

    @Test
    fun getUserWithNoSimpleWorkouts() = runBlocking {
        val insertUser = SeedData.testUsers.first()
        val userId = userDao.insertUser(insertUser)
        val expectedUserWithSimpleWorkouts = SeedData.makeTestUserWithSimpleWorkouts(
            makeTestUser(userId),
            listOf()
        )

        val userWithSimpleWorkouts = userDao.getUserWithSimpleWorkouts(userId).awaitValue()
        assert(userWithSimpleWorkouts.user == expectedUserWithSimpleWorkouts.user)
        assert(userWithSimpleWorkouts.workouts == expectedUserWithSimpleWorkouts.workouts)
    }

    @Test
    fun getUserWithSimpleWorkouts() = runBlocking {
        val expectedUser = SeedData.testUsers.map {
            val uid = userDao.insertUser(it)
            makeTestUser(uid)
        }.first()
        val expectedWorkouts = SeedData.testWorkouts.map {
            val wid = workoutDao.insertWorkout(it)
            makeTestWorkout(wid)
        }.sortedBy { it.workoutId }

        val userWithSimpleWorkouts = userDao.getUserWithSimpleWorkouts(expectedUser.userId!!).awaitValue()
        val workouts = userWithSimpleWorkouts.workouts.sortedBy { it.workoutId }
        assert(userWithSimpleWorkouts.user == expectedUser)
        assert(workouts == expectedWorkouts)
    }

//    private suspend fun insertTestUsers(userDao: UserDao, users: List<User>) = runBlocking {
//        users.map { makeTestUser(userDao.insertUser(it)) }
//    }
}