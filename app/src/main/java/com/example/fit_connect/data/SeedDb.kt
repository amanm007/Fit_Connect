package com.example.fit_connect.data

import android.content.Context
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fit_connect.data.FitConnectDatabase.Companion.getInstance
import com.example.fit_connect.data.user.User
import com.example.fit_connect.data.workout.Exercise
import com.example.fit_connect.data.workout.ExerciseSet
import com.example.fit_connect.data.workout.ExerciseType
import com.example.fit_connect.data.workout.ExerciseTypeEnum
import com.example.fit_connect.data.workout.Workout
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.concurrent.Executors

class SeedDb {
    fun seed(context: Context): Callback = object : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Executors.newSingleThreadExecutor().execute {
                runBlocking {
                    seedData(getInstance(context))
                }
            }
        }
    }

    suspend fun seedData(db: FitConnectDatabase) {
        val workoutDao = db.workoutDao()
        val userDao = db.userDao()

        val exerciseTypes = ExerciseTypeEnum.entries.map { ExerciseType(it) }
        val exerciseTypeIds = workoutDao.insertExerciseTypes(exerciseTypes)

        val dummyUser = User(
            "testFirst",
            "testLast",
            "test",
            "devtasks008@gmail.com",
            "123456",
            0
        )
        val dummyUserId = userDao.insertUser(dummyUser)

        val calendar = Calendar.getInstance()
        val today = calendar.time.time
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = calendar.time.time
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val twoDaysAgo = calendar.time.time

        val workouts = listOf(
            Workout(
                today,
                60,
                false,
                userId = dummyUserId
            ),
            Workout(
                yesterday,
                50,
                false,
                userId = dummyUserId
            ),
            Workout(
                twoDaysAgo,
                80,
                false,
                userId = dummyUserId
            )
        )
        val workoutIds = workouts.map { w -> workoutDao.insertWorkout(w) }

        val exercises = listOf(
            Exercise(
                5,
                "",
                exerciseTypeId = exerciseTypeIds.first(),
                workoutId = workoutIds.first()
            ),
            Exercise(
                5,
                "",
                exerciseTypeId = exerciseTypeIds[1],
                workoutId = workoutIds.first()
            ),
            Exercise(
                5,
                "",
                exerciseTypeId = exerciseTypeIds[2],
                workoutId = workoutIds[1]
            ),
            Exercise(
                5,
                "",
                exerciseTypeId = exerciseTypeIds[3],
                workoutId = workoutIds[2]
            )
        )
        val exerciseIds = exercises.map { ex -> workoutDao.insertExercise(ex) }

        val sets = listOf(
            ExerciseSet(
                10,
                5,
                exerciseId = exerciseIds.first()
            ),
            ExerciseSet(
                20,
                5,
                exerciseId = exerciseIds.first()
            ),
            ExerciseSet(
                8,
                10,
                exerciseId = exerciseIds[1]
            ),
            ExerciseSet(
                5,
                15,
                exerciseId = exerciseIds[1]
            ),
            ExerciseSet(
                15,
                5,
                exerciseId = exerciseIds[1]
            ),
            ExerciseSet(
                20,
                6,
                exerciseId = exerciseIds[2]
            ),
            ExerciseSet(
                5,
                10,
                exerciseId = exerciseIds[3]
            ),
            ExerciseSet(
                5,
                15,
                exerciseId = exerciseIds[3]
            )
        )
        val setIds = sets.map { s -> workoutDao.insertSet(s) }
    }
}