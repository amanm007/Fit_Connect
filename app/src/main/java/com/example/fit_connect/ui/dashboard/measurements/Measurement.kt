package com.example.fit_connect.ui.dashboard.measurements

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurements")
data class Measurement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long,
    val bodyWeight: Float?, // lbs
    val waist: Float?, // cm
    val bodyFat: Float?, // %
    val neck: Float?, // cm
    val shoulder: Float?, // cm
    val chest: Float?, // cm

    val bicep: Float?, // cm
    val Forearm: Float?, // cm

    val abdomen: Float? // cm
)
