package com.example.fit_connect.ui.dashboard.measurements

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MeasurementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: Measurement)

    @Query("SELECT * FROM measurements ORDER BY date DESC")
    fun getAllMeasurements(): LiveData<List<Measurement>>


}
