package com.example.fit_connect.ui.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.fit_connect.WorkoutRecord
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.workout.ExerciseWithSets
import com.example.fit_connect.data.workout.Workout
import com.example.fit_connect.data.workout.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class DashboardViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {
    private val db = FitConnectDatabase.getInstance(application)
    private val workoutRepo = WorkoutRepository(db.workoutDao())

    private val _weeklyRecords = MutableLiveData<List<WorkoutRecord>>()
    val weeklyRecords: LiveData<List<WorkoutRecord>> get() = _weeklyRecords

    init {
        // TEST GET LAST 3 DAYS
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -3)
        val threeDaysAgo = calendar.time

        loadWeeklyRecords(threeDaysAgo, Date())
    }

    private fun loadWeeklyRecords(startDate: Date, endDate: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                viewModelScope.launch {
                    workoutRepo.getUserWorkoutsInDateRange(userId, startDate, endDate)
                        .asFlow()
                        .collect { workouts ->
                            workoutRepo.getWorkoutWithExercisesAndSets(workouts.map { it.workoutId!! })
                                .asFlow()
                                .collect { workoutRecords ->
                                    _weeklyRecords.postValue(mapWorkoutRecords(workoutRecords))
                                }
                        }
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error saving workout: ${e.message}", e)
            }
        }
    }

    private fun mapWorkoutRecords(userWorkoutRecords: Map<Workout, List<ExerciseWithSets>>): List<WorkoutRecord>
        = userWorkoutRecords.entries.map { (workout, exerciseSets) ->
            WorkoutRecord(
                date = workout.timestamp,
                duration = workout.duration,
                volume = exerciseSets.sumOf { exerciseSet ->
                    exerciseSet.sets.sumOf { set -> set.volume * set.reps }
                },
                reps = exerciseSets.sumOf { exerciseSet ->
                    exerciseSet.sets.sumOf { set -> set.reps }
                }
            )
        }
}

class DashboardViewModelFactory(
    private val application: Application,
    private val userId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DashboardViewModel(application, userId) as T
    }
}
