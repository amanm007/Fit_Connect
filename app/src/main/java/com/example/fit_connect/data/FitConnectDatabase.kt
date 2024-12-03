package com.example.fit_connect.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fit_connect.data.routine.RoutineDao
import com.example.fit_connect.data.routine.RoutineExercise
import com.example.fit_connect.data.routine.RoutineExerciseSet
import com.example.fit_connect.data.routine.RoutineWorkout
import com.example.fit_connect.data.user.Following
import com.example.fit_connect.data.user.User
import com.example.fit_connect.data.user.UserDao
import com.example.fit_connect.data.workout.Exercise
import com.example.fit_connect.data.workout.ExerciseSet
import com.example.fit_connect.data.workout.ExerciseType
import com.example.fit_connect.data.workout.ExerciseTypeConverter
import com.example.fit_connect.data.workout.Workout
import com.example.fit_connect.data.workout.WorkoutConverter
import com.example.fit_connect.data.workout.WorkoutDao

const val DB_NAME = "fit_connect_db"

@Database(
    entities = [
        User::class,
        Following::class,
        Workout::class,
        ExerciseType::class,
        Exercise::class,
        ExerciseSet::class,
        RoutineWorkout::class,
        RoutineExercise::class,
        RoutineExerciseSet::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExerciseTypeConverter::class, WorkoutConverter::class)
abstract class FitConnectDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun routineDao(): RoutineDao

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
                    .addCallback(SeedDb().seed(context))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}