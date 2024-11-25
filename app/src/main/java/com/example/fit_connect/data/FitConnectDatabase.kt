package com.example.fit_connect.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fit_connect.data.user.User
import com.example.fit_connect.data.user.UserDao
import com.example.fit_connect.data.workout.Exercise
import com.example.fit_connect.data.workout.ExerciseSet
import com.example.fit_connect.data.workout.ExerciseType
import com.example.fit_connect.data.workout.ExerciseTypeConverter
import com.example.fit_connect.data.workout.Workout
import com.example.fit_connect.data.workout.WorkoutDao
import com.example.fit_connect.data.workout.WorkoutToExercise

const val DB_NAME = "fit_connect_db"

@Database(
    entities = [
        User::class,
        Workout::class,
        ExerciseType::class,
        Exercise::class,
        ExerciseSet::class,
        WorkoutToExercise::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExerciseTypeConverter::class)
abstract class FitConnectDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: FitConnectDatabase? = null

        fun getInstance(context: Context): FitConnectDatabase {
            synchronized(this) {
                val instance = INSTANCE ?: Room
                    .databaseBuilder(
                        context.applicationContext,
                        FitConnectDatabase::class.java,
                        DB_NAME
                    )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}