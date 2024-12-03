package com.example.fit_connect.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class ExerciseTypeEnum(val id: Int, val displayName: String, val category: String) {
    BenchPress(0, "Bench Press", "Chest"),
    Squat(1, "Squat", "Legs"),
    DeadLift(2, "Dead Lift", "Back"),
    OverheadPress(3, "Overhead Press", "Shoulders"),
    PullUps(4, "Pull Ups", "Back"),
    PushUps(5, "Push Ups", "Chest"),
    BarbellRow(6, "Barbell Row", "Back"),
    DumbbellCurl(7, "Dumbbell Curl", "Arms")
}

const val EXERCISE_TYPE_TABLE_NAME = "exerciseType"
const val EXERCISE_TYPE_ID_NAME = "exercise_type_id"

@Entity(tableName = EXERCISE_TYPE_TABLE_NAME)
data class ExerciseType(
    val type: ExerciseTypeEnum,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = EXERCISE_TYPE_ID_NAME)
    val exerciseTypeId: Long? = null
)

class ExerciseTypeConverter {
    @TypeConverter
    fun toExerciseType(exerciseTypeName: String): ExerciseTypeEnum = enumValueOf(exerciseTypeName)

    @TypeConverter
    fun fromExerciseType(type: ExerciseTypeEnum): String = type.name
}
