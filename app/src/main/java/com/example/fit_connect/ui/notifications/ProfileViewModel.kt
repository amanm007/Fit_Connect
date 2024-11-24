package com.example.fit_connect.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit_connect.AppDatabase
import com.example.fit_connect.WorkoutRecord
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val workoutDao = database.workoutRecordDao()

    private val _weeklyRecords = MutableLiveData<List<WorkoutRecord>>()
    val weeklyRecords: LiveData<List<WorkoutRecord>> = _weeklyRecords

    fun loadWeeklyData(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            _weeklyRecords.value = workoutDao.getWeeklyRecords(startDate, endDate)
        }
    }
}
