package com.example.fit_connect.data.workout

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Insert
    suspend fun insertExerciseTypes(exerciseTypes: List<ExerciseType>): List<Long>

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Insert
    suspend fun insertSet(set: ExerciseSet): Long

    @Query("SELECT * FROM $EXERCISE_TYPE_TABLE_NAME WHERE $EXERCISE_TYPE_ID_NAME = :exerciseTypeId")
    fun getExerciseType(exerciseTypeId: Long): LiveData<ExerciseType?>

    @Query("SELECT * FROM $EXERCISE_TYPE_TABLE_NAME")
    fun getAllExerciseTypes(): LiveData<List<ExerciseType>>

    @Transaction
    @Query("SELECT * FROM $WORKOUT_TABLE_NAME WHERE $WORKOUT_ID_NAME = :workoutId")
    fun getWorkoutWithExercises(workoutId: Long): LiveData<WorkoutWithExercises>

    @Query("SELECT * FROM $WORKOUT_TABLE_NAME WHERE user_id = :userId AND timestamp BETWEEN :startDate AND :endDate")
    fun getUserWorkoutsInDateRange(userId: Long, startDate: Long, endDate: Long): LiveData<List<Workout>>

    @Transaction
    @Query("SELECT * FROM $EXERCISE_TABLE_NAME WHERE $EXERCISE_ID_NAME = :exerciseId")
    fun getExerciseWithSets(exerciseId: Long): LiveData<ExerciseWithSets>
}