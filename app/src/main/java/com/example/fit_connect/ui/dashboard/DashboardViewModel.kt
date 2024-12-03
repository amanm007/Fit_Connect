package com.example.fit_connect.ui.dashboard

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.fit_connect.AppDatabase
import com.example.fit_connect.WorkoutRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val workoutDao = AppDatabase.getDatabase(application).workoutRecordDao()

    private val _weeklyRecords = MutableLiveData<List<WorkoutRecord>>()
    val weeklyRecords: LiveData<List<WorkoutRecord>> get() = _weeklyRecords

    init {
        addFakeData()

    }

    fun loadWeeklyRecords(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val records = workoutDao.getWeeklyRecords(startDate, endDate)
            _weeklyRecords.postValue(records)
        }
    }


    private fun addFakeData() {
        viewModelScope.launch(Dispatchers.IO) {

            val fakeData = listOf(
                WorkoutRecord(date = System.currentTimeMillis(), duration = 60, volume = 5000, reps = 100),
                WorkoutRecord(date = System.currentTimeMillis() - 86400000, duration = 45, volume = 3000, reps = 80),
                WorkoutRecord(date = System.currentTimeMillis() - 172800000, duration = 30, volume = 2000, reps = 60),
                WorkoutRecord(date = System.currentTimeMillis() - 259200000, duration = 50, volume = 4000, reps = 90),
                WorkoutRecord(date = System.currentTimeMillis() - 345600000, duration = 40, volume = 3500, reps = 70),
                WorkoutRecord(date = System.currentTimeMillis() - 432000000, duration = 35, volume = 2500, reps = 65),
                WorkoutRecord(date = System.currentTimeMillis() - 518400000, duration = 55, volume = 4500, reps = 95)
            )

            // Insert mock data into the database
            fakeData.forEach {
                workoutDao.insert(it)
                android.util.Log.d("DashboardViewModel", "Inserted WorkoutRecord: $it")
            }
        }
    }
}
