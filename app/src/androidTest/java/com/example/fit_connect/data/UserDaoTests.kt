package com.example.fit_connect.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.fit_connect.awaitValue
import com.example.fit_connect.data.user.UserDao
import kotlinx.coroutines.runBlocking
import com.example.fit_connect.TestUtil
import com.example.fit_connect.data.SeedTestData.Companion.insertTestUser
import com.example.fit_connect.data.SeedTestData.Companion.insertTestUsers
import com.example.fit_connect.data.SeedTestData.Companion.insertTestWorkouts
import com.example.fit_connect.data.SeedTestData.Companion.makeTestUser
import com.example.fit_connect.data.user.User
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
        db = TestUtil.createTestDbBuilder(context).build()
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
        val expectedUsers = insertTestUsers(userDao, SeedTestData.testUsers).sortedBy { it.userId }

        val users = userDao.getAllUsers().awaitValue().sortedBy { it.userId }
        assertUsersEqual(users, expectedUsers)
    }

    @Test
    fun testGetUserById() = runBlocking {
        val expectedUser = insertTestUsers(userDao, SeedTestData.testUsers).first()

        val user = userDao.getUser(expectedUser.userId!!).awaitValue()
        assert(user != null)
        assertUserEqual(user, expectedUser)
    }

    @Test
    fun testInsertUser() = runBlocking {
        val expectedUser = insertTestUser(userDao, SeedTestData.testUsers.first())

        val users = userDao.getAllUsers().awaitValue()
        assert(users.size == 1)
        assertUserEqual(users.first(), expectedUser)
    }

    @Test
    fun getUserWithNoSimpleWorkouts() = runBlocking {
        val insertUser = SeedTestData.testUsers.first()
        val userId = userDao.insertUser(insertUser)
        val expectedUserWithSimpleWorkouts = SeedTestData.makeTestUserWithSimpleWorkouts(
            makeTestUser(userId),
            listOf()
        )

        val userWithSimpleWorkouts = userDao.getUserWithSimpleWorkouts(userId).awaitValue()
        assertUserEqual(userWithSimpleWorkouts.user, expectedUserWithSimpleWorkouts.user)
        assert(userWithSimpleWorkouts.workouts == expectedUserWithSimpleWorkouts.workouts)
    }

    @Test
    fun getUserWithSimpleWorkouts() = runBlocking {
        val expectedUser = insertTestUsers(userDao, SeedTestData.testUsers).first()
        val expectedWorkouts = insertTestWorkouts(workoutDao, expectedUser.userId!!, SeedTestData.testWorkouts).sortedBy { it.workoutId }

        val userWithSimpleWorkouts = userDao.getUserWithSimpleWorkouts(expectedUser.userId!!).awaitValue()
        val workouts = userWithSimpleWorkouts.workouts.sortedBy { it.workoutId }
        assertUserEqual(userWithSimpleWorkouts.user, expectedUser)
        assert(workouts == expectedWorkouts)
    }

    private fun assertUsersEqual(u1: List<User?>, u2: List<User?>) {
        assert(u1.size == u2.size)
        u1.forEachIndexed { i, u -> assertUserEqual(u, u2[i]) }
    }

    private fun assertUserEqual(u1: User?, u2: User?) {
        assert(u1!!.userId == u2!!.userId)
        assert(u1.userName == u2.userName)
    }
}