package com.example.fit_connect.data

import com.example.fit_connect.data.user.User
import com.example.fit_connect.data.user.UserDao
import com.example.fit_connect.data.user.UserWithSimpleWorkout
import com.example.fit_connect.data.workout.Exercise
import com.example.fit_connect.data.workout.ExerciseType
import com.example.fit_connect.data.workout.ExerciseTypeEnum
import com.example.fit_connect.data.workout.Workout
import com.example.fit_connect.data.workout.WorkoutDao
import java.util.Date

class SeedTestData {
    companion object {
        suspend fun insertTestUsers(userDao: UserDao, users: List<User>)
            = users.map { insertTestUser(userDao, it) }
        suspend fun insertTestUser(userDao: UserDao, user: User)
            = makeTestUser(userDao.insertUser(user))
        fun makeTestUser(id: Long? = null): User
            = User("testFirst", "testLast", "testEmail", id)
        val testUsers = listOf(
            makeTestUser(1),
            makeTestUser(2),
            makeTestUser(3)
        )

        suspend fun insertTestWorkouts(workoutDao: WorkoutDao, userId: Long, workouts: List<Workout>)
            = workouts.map { insertTestWorkout(workoutDao, userId, it) }
        suspend fun insertTestWorkout(workoutDao: WorkoutDao, userId: Long, workout: Workout): Workout {
            val w = makeTestWorkout(userId, workout.workoutId)
            val wId = workoutDao.insertWorkout(w)
            return makeTestWorkout(userId, wId)
        }
        fun makeTestWorkout(userId: Long, id: Long? = null): Workout
            = Workout(Date(Long.MIN_VALUE).time, 1, true, id, userId)
        val testWorkouts = listOf(
            makeTestWorkout(testUsers.first().userId!!, 1),
            makeTestWorkout(testUsers.first().userId!!, 2)
        )

        fun makeTestUserWithSimpleWorkouts(user: User, workouts: List<Workout>)
            = UserWithSimpleWorkout(user, workouts)

        suspend fun insertExerciseTypes(workoutDao: WorkoutDao, exerciseTypes: List<ExerciseType>)
            = exerciseTypes.map { insertExerciseType(workoutDao, it) }
        suspend fun insertExerciseType(workoutDao: WorkoutDao, exerciseType: ExerciseType): ExerciseType {
            val exerciseTypeId = workoutDao.insertExerciseTypes(listOf(exerciseType)).first()
            return makeTestExerciseType(exerciseType.type, exerciseTypeId)
        }
        fun makeTestExerciseType(type: ExerciseTypeEnum, id: Long? = null)
            = ExerciseType(type, id)
        val testExerciseTypes = listOf(
            makeTestExerciseType(ExerciseTypeEnum.PullUps, 1),
            makeTestExerciseType(ExerciseTypeEnum.Squat, 2)
        )

        suspend fun insertTestExercises(workoutDao: WorkoutDao, workoutId: Long, exerciseTypeId: Long, exercises: List<Exercise>)
            = exercises.map { insertTestExercise(workoutDao, workoutId, exerciseTypeId, it) }
        suspend fun insertTestExercise(workoutDao: WorkoutDao, workoutId: Long, exerciseTypeId: Long, exercise: Exercise): Exercise {
            val e = makeTestExercise(workoutId, exerciseTypeId, exercise.exerciseId)
            val eId = workoutDao.insertExercise(e)
            return makeTestExercise(workoutId, exerciseTypeId, eId)
        }
        fun makeTestExercise(workoutId: Long, exerciseTypeId: Long, id: Long? = null)
            = Exercise(1, "", id, exerciseTypeId, workoutId)
        val testExercises = listOf(
            makeTestExercise(testWorkouts.first().workoutId!!, testExerciseTypes.first().exerciseTypeId!!, 1),
            makeTestExercise(testWorkouts.first().workoutId!!, testExerciseTypes[1].exerciseTypeId!!, 2)
        )
    }
}