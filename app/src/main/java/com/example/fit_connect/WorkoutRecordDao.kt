package com.example.fit_connect

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkoutRecordDao {
    @Query("SELECT * FROM workout_records WHERE date BETWEEN :startDate AND :endDate")
    fun getWeeklyRecords(startDate: Long, endDate: Long): List<WorkoutRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workoutRecord: WorkoutRecord)
}
