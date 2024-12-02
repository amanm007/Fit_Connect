package com.example.fit_connect.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.fit_connect.TestUtil
import com.example.fit_connect.awaitValue
import com.example.fit_connect.data.SeedTestData.Companion.insertExerciseType
import com.example.fit_connect.data.SeedTestData.Companion.insertExerciseTypes
import com.example.fit_connect.data.SeedTestData.Companion.insertTestExercises
import com.example.fit_connect.data.SeedTestData.Companion.insertTestUser
import com.example.fit_connect.data.SeedTestData.Companion.insertTestWorkout
import com.example.fit_connect.data.user.UserDao
import com.example.fit_connect.data.workout.ExerciseTypeEnum
import com.example.fit_connect.data.workout.WorkoutDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WorkoutDaoTests {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: FitConnectDatabase
    private lateinit var workoutDao: WorkoutDao
    private lateinit var userDao: UserDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = TestUtil.createTestDbBuilder(context).build()
        workoutDao = db.workoutDao()
        userDao = db.userDao()
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun testGetAllExerciseTypes() = runBlocking {
        SeedDb().seedData(db)
        val expectedExerciseTypeEnums = ExerciseTypeEnum.entries.sorted()

        val exerciseTypes = workoutDao.getAllExerciseTypes().awaitValue()
        val exerciseTypeEnums = exerciseTypes.map { it.type }.sorted()

        assert(expectedExerciseTypeEnums == exerciseTypeEnums)
    }

    @Test
    fun testGetExerciseType() = runBlocking {
        val expectedExerciseType = insertExerciseTypes(workoutDao, SeedTestData.testExerciseTypes).first()

        val exerciseType = workoutDao.getExerciseType(expectedExerciseType.exerciseTypeId!!).awaitValue()
        assert(expectedExerciseType == exerciseType)
    }

    @Test
    fun testWorkoutWithNoExercises() = runBlocking {
        val user = insertTestUser(userDao, SeedTestData.testUsers.first())
        val expectedWorkout = insertTestWorkout(workoutDao, user.userId!!, SeedTestData.testWorkouts.first())

        val workoutWithExercises = workoutDao.getWorkoutWithExercises(expectedWorkout.workoutId!!).awaitValue()
        assert(workoutWithExercises.workout == expectedWorkout)
        assert(workoutWithExercises.exercises.isEmpty())
    }

    @Test
    fun testWorkoutWithExercises() = runBlocking {
        val user = insertTestUser(userDao, SeedTestData.testUsers.first())
        val exerciseType = insertExerciseType(workoutDao, SeedTestData.testExerciseTypes.first())

        val expectedWorkout = insertTestWorkout(workoutDao, user.userId!!, SeedTestData.testWorkouts.first())
        val expectedExercises = insertTestExercises(
            workoutDao,
            expectedWorkout.workoutId!!,
            exerciseType.exerciseTypeId!!,
            SeedTestData.testExercises
        ).sortedBy { it.exerciseId }

        val workoutWithExercises = workoutDao.getWorkoutWithExercises(expectedWorkout.workoutId!!).awaitValue()
        val exercises = workoutWithExercises.exercises.sortedBy { it.exerciseId }
        assert(workoutWithExercises.workout == expectedWorkout)
        assert(exercises == expectedExercises)
    }
}