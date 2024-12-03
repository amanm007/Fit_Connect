package com.example.fit_connect.data.routine

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fit_connect.data.workout.EXERCISE_ID_NAME

@Dao
interface RoutineDao {
    @Insert
    suspend fun insertWorkout(workout: RoutineWorkout): Long

    @Insert
    suspend fun insertExercise(exercise: RoutineExercise): Long

    @Insert
    suspend fun insertSet(set: RoutineExerciseSet): Long

    @Update
    suspend fun updateWorkout(workout: RoutineWorkout) : Int

    @Query("SELECT * FROM $ROUTINE_WORKOUT_TABLE_NAME WHERE $WORKOUT_ID_NAME = :workoutId")
    fun getWorkout(workoutId : Long ): LiveData<RoutineWorkout?>

    @Transaction
    @Query("SELECT * FROM $ROUTINE_WORKOUT_TABLE_NAME WHERE $WORKOUT_ID_NAME = :workoutId")
    fun getWorkoutWithExercises(workoutId: Long): LiveData<RoutineWorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM $ROUTINE_EXERCISE_TABLE_NAME WHERE $EXERCISE_ID_NAME = :exerciseId")
    fun getExerciseWithSets(exerciseId: Long): LiveData<RoutineExerciseWithSets>
}