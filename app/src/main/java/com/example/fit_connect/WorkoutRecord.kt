package com.example.fit_connect

import com.example.fit_connect.data.workout.ExerciseWithSets
import com.example.fit_connect.data.workout.Workout

data class WorkoutRecord(
    val date: Long,     // timestamp
    val duration: Int,  // Duration in minutes
    val volume: Int,    // Volume in lbs/kgs = weight * reps for each set
    val reps: Int // Repetitions = reps added up across all exercises in a workout
)
fun helperMapWorkoutRecords(userWorkoutRecords: Map<Workout, List<ExerciseWithSets>>): List<WorkoutRecord>
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
