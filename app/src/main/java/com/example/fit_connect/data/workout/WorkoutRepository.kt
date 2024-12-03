package com.example.fit_connect.data.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Date

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

    fun getWorkout(workoutId : Long) = workoutDao.getWorkout(workoutId)

    fun getExerciseType(exerciseTypeId: Long)
        = workoutDao.getExerciseType(exerciseTypeId)

    fun getAllExerciseTypes()
        = workoutDao.getAllExerciseTypes()

    fun getWorkoutWithExercises(workoutId: Long)
        = workoutDao.getWorkoutWithExercises(workoutId)

    fun getWorkoutsWithExercises(workoutIds: List<Long>): LiveData<List<WorkoutWithExercises>> {
        val mediatorResult = MediatorLiveData<List<WorkoutWithExercises>>()
        val results = mutableListOf<WorkoutWithExercises>()
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

    fun getExercisesWithSets(exerciseIds: List<Long>): LiveData<List<ExerciseWithSets>> {
        val mediatorResult = MediatorLiveData<List<ExerciseWithSets>>()
        val results = mutableListOf<ExerciseWithSets>()
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

    fun getWorkoutWithExercisesAndSets(workoutIds: List<Long>): LiveData<Map<Workout, List<ExerciseWithSets>>> {
        val resultMediator = MediatorLiveData<Map<Workout, List<ExerciseWithSets>>>()
        val result = mutableMapOf<Workout, List<ExerciseWithSets>>()
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

    fun getUserWorkoutsInDateRange(userId: Long, startDate: Date, endDate: Date)
        = workoutDao.getUserWorkoutsInDateRange(userId, startDate.time, endDate.time)

    fun getUserWorkoutDays(userId: Long)
        = workoutDao.getUserWorkoutDays(userId).map { workoutDays ->
            workoutDays.map { LocalDate.parse(it) }
        }
}