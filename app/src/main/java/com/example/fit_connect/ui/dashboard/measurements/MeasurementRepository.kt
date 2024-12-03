package com.example.fit_connect.ui.dashboard.measurements

import androidx.lifecycle.LiveData

class MeasurementRepository(private val measurementDao: MeasurementDao) {
    suspend fun insertMeasurement(measurement: Measurement) {
        measurementDao.insertMeasurement(measurement)
    }

    fun getAllMeasurements(): LiveData<List<Measurement>> {
        return measurementDao.getAllMeasurements()
    }
}
