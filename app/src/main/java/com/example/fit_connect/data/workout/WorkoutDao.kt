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
    suspend fun insertExerciseTypes(exerciseTypes: List<ExerciseType>)

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Insert
    suspend fun insertSet(set: ExerciseSet): Long

    @Transaction
    @Query("SELECT * FROM $EXERCISE_TYPE_TABLE_NAME WHERE $EXERCISE_TYPE_ID_NAME = :exerciseTypeId")
    fun getExerciseType(exerciseTypeId: Long): LiveData<ExerciseType?>

    @Transaction
    @Query("SELECT * FROM $WORKOUT_TO_EXERCISE_TABLE_NAME WHERE $WORKOUT_ID_NAME = :workoutId")
    fun getWorkoutWithExercises(workoutId: Long): LiveData<WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM $EXERCISE_TO_SET_TABLE_NAME WHERE $EXERCISE_ID_NAME = :exerciseId")
    fun getExerciseWithSets(exerciseId: Long): LiveData<ExerciseWithSets>
}