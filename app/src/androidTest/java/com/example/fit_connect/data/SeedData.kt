package com.example.fit_connect.data

import com.example.fit_connect.data.user.User
import com.example.fit_connect.data.user.UserWithSimpleWorkout
import com.example.fit_connect.data.workout.Workout
import java.time.Instant
import java.util.Date

class SeedData {
    companion object {
        fun makeTestUser(id: Long? = null): User
            = User("testFirst", "testLast", "testEmail", id)

        val testUsers = listOf(
            makeTestUser(1),
            makeTestUser(2),
            makeTestUser(3)
        )

        fun makeTestWorkout(userId: Long, id: Long? = null): Workout
            = Workout(Date.from(Instant.MIN).time, 1, true, id, userId)

        val testWorkouts = listOf(
            makeTestWorkout(testUsers.first().userId!!, 1),
            makeTestWorkout(testUsers.first().userId!!, 2)
        )

        fun makeTestUserWithSimpleWorkouts(user: User, workouts: List<Workout>)
            = UserWithSimpleWorkout(user, workouts)

        val testUserWithSimpleWorkouts = listOf(
            makeTestUserWithSimpleWorkouts(testUsers.first(), testWorkouts)
        )
    }
}