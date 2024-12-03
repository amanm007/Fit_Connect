package com.example.fit_connect.data.routine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class RoutineRepository(private val workoutDao: RoutineDao) {
    suspend fun insertWorkout(workout: RoutineWorkout): Long
        = withContext(IO) { workoutDao.insertWorkout(workout) }

    suspend fun insertExercise(exercise: RoutineExercise): Long
        = withContext(IO) { workoutDao.insertExercise(exercise) }

    suspend fun insertSet(set: RoutineExerciseSet): Long
        = withContext(IO) { workoutDao.insertSet(set) }

    suspend fun updateWorkout(workout: RoutineWorkout): Int
        = withContext(IO) {workoutDao.updateWorkout(workout)}

    fun getWorkout(workoutId : Long)
        = workoutDao.getWorkout(workoutId)

    fun getWorkoutWithExercises(workoutId: Long)
        = workoutDao.getWorkoutWithExercises(workoutId)

    fun getWorkoutsWithExercises(workoutIds: List<Long>): LiveData<List<RoutineWorkoutWithExercises>> {
        val mediatorResult = MediatorLiveData<List<RoutineWorkoutWithExercises>>()
        val results = mutableListOf<RoutineWorkoutWithExercises>()
        val numWorkouts = workoutIds.size

        workoutIds.forEach { wId ->
            val liveResult = getWorkoutWithExercises(wId)
            mediatorResult.addSource(liveResult) { workoutWithExercises ->
                results.add(workoutWithExercises)
                if(results.size == numWorkouts)
                    mediatorResult.value = results.toList()
            }
        }

        return mediatorResult
    }

    fun getExerciseWithSets(exerciseId: Long)
        = workoutDao.getExerciseWithSets(exerciseId)

    fun getExercisesWithSets(exerciseIds: List<Long>): LiveData<List<RoutineExerciseWithSets>> {
        val mediatorResult = MediatorLiveData<List<RoutineExerciseWithSets>>()
        val results = mutableListOf<RoutineExerciseWithSets>()
        val numExercises = exerciseIds.size

        exerciseIds.forEach { exId ->
            val liveResult = getExerciseWithSets(exId)
            mediatorResult.addSource(liveResult) { exerciseWithSets ->
                results.add(exerciseWithSets)
                if(results.size == numExercises)
                    mediatorResult.value = results.toList()
            }
        }

        return mediatorResult
    }

    fun getWorkoutWithExercisesAndSets(workoutIds: List<Long>): LiveData<Map<RoutineWorkout, List<RoutineExerciseWithSets>>> {
        val resultMediator = MediatorLiveData<Map<RoutineWorkout, List<RoutineExerciseWithSets>>>()
        val result = mutableMapOf<RoutineWorkout, List<RoutineExerciseWithSets>>()
        val numWorkouts = workoutIds.size

        val liveWorkoutWithExercises = getWorkoutsWithExercises(workoutIds)
        resultMediator.addSource(liveWorkoutWithExercises) { workoutsWithExercises ->
            workoutsWithExercises
                .filter { it.workout != null }
                .forEach {
                    val exercises = it.exercises
                    val workout = it.workout!!

                    val liveExercisesWithSets = getExercisesWithSets(exercises.map { it.exerciseId!! })
                    resultMediator.addSource(liveExercisesWithSets) { exercisesWithSets ->
                        result[workout] = exercisesWithSets
                        if(numWorkouts == result.size)
                            resultMediator.value = result.toMap()
                    }
                }
        }

        return resultMediator
    }
}