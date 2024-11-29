package com.example.fit_connect.data

import com.example.fit_connect.data.user.User
import com.example.fit_connect.data.user.UserWithSimpleWorkout
import com.example.fit_connect.data.workout.ExerciseType
import com.example.fit_connect.data.workout.ExerciseTypeEnum
import com.example.fit_connect.data.workout.Workout
import java.util.Date

class SeedTestData {
    companion object {
        fun makeTestUser(id: Long? = null): User
            = User("testFirst", "testLast", "testEmail", id)

        val testUsers = listOf(
            makeTestUser(1),
            makeTestUser(2),
            makeTestUser(3)
        )

        fun makeTestWorkout(userId: Long, id: Long? = null): Workout
            = Workout(Date(Long.MIN_VALUE).time, 1, true, id, userId)

        val testWorkouts = listOf(
            makeTestWorkout(testUsers.first().userId!!, 1),
            makeTestWorkout(testUsers.first().userId!!, 2)
        )

        fun makeTestUserWithSimpleWorkouts(user: User, workouts: List<Workout>)
            = UserWithSimpleWorkout(user, workouts)

        val exerciseTypes = listOf(
            makeExerciseType(ExerciseTypeEnum.PullUp, 1),
            makeExerciseType(ExerciseTypeEnum.HammerCurl, 2)
        )

        fun makeExerciseType(type: ExerciseTypeEnum, id: Long? = null)
            = ExerciseType(type, id)
    }
}