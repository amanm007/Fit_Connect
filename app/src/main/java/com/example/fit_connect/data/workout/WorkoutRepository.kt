package com.example.fit_connect.data.workout

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    suspend fun insertWorkout(workout: Workout): Long
        = withContext(IO) { workoutDao.insertWorkout(workout) }

    suspend fun insertExerciseTypes(exerciseTypes: List<ExerciseType>)
        = withContext(IO) { workoutDao.insertExerciseTypes(exerciseTypes) }

    suspend fun insertExercise(exercise: Exercise): Long
        = withContext(IO) { workoutDao.insertExercise(exercise) }

    suspend fun insertSet(set: ExerciseSet): Long
        = withContext(IO) { workoutDao.insertSet(set) }

    suspend fun updateWorkout(workout: Workout): Int
        = withContext(IO) {workoutDao.updateWorkout(workout)}

    fun getExerciseType(exerciseTypeId: Long)
        = workoutDao.getExerciseType(exerciseTypeId)

    fun getWorkoutWithExercises(workoutId: Long)
        = workoutDao.getWorkoutWithExercises(workoutId)

    fun getExerciseWithSets(exerciseId: Long)
        = workoutDao.getExerciseWithSets(exerciseId)
}