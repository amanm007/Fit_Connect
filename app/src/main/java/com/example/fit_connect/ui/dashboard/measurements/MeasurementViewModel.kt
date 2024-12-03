package com.example.fit_connect.ui.dashboard.measurements

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fit_connect.data.FitConnectDatabase
import kotlinx.coroutines.launch

class MeasurementViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MeasurementRepository

    val measurements: LiveData<List<Measurement>>

    init {
        val measurementDao = FitConnectDatabase.getInstance(application).measurementDao()
        repository = MeasurementRepository(measurementDao)
        measurements = repository.getAllMeasurements()
    }

    fun insertMeasurement(measurement: Measurement) {
        viewModelScope.launch {
            repository.insertMeasurement(measurement)
            Log.d("MeasurementDebug", "Inserting: $measurement")
        }
    }
}