package com.example.fit_connect.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class ExerciseTypeEnum(val id: Int) {
    PullUp(0),
    StandingCalfRaises(1),
    LegExtensions(2),
    HammerCurl(3)
}

const val EXERCISE_TYPE_TABLE_NAME = "exerciseType"
const val EXERCISE_TYPE_ID_NAME = "exercise_type_id"

@Entity(tableName = EXERCISE_TYPE_TABLE_NAME)
data class ExerciseType(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = EXERCISE_TYPE_ID_NAME)
    val exerciseTypeId: Long? = null
)

class ExerciseTypeConverter {
    @TypeConverter
    fun fromExerciseType(exerciseType: ExerciseType): Long? {
        return exerciseType.exerciseTypeId
    }

    @TypeConverter
    fun toExerciseType(id: Int): ExerciseTypeEnum? {
        return ExerciseTypeEnum.entries.find { it.id == id }
    }
}
