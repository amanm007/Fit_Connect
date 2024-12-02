package com.example.fit_connect.data

import android.content.Context
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fit_connect.data.FitConnectDatabase.Companion.getInstance
import com.example.fit_connect.data.workout.ExerciseType
import com.example.fit_connect.data.workout.ExerciseTypeEnum
import kotlinx.coroutines.runBlocking
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
        val exerciseTypes = ExerciseTypeEnum.entries.map { ExerciseType(it) }
        db.workoutDao().insertExerciseTypes(exerciseTypes)
    }
}