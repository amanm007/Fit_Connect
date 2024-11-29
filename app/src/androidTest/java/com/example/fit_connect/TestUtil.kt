package com.example.fit_connect

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fit_connect.data.FitConnectDatabase
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
class TestUtil {
    companion object {
        fun createTestDbBuilder(context: Context): RoomDatabase.Builder<FitConnectDatabase> {
            return Room
                .inMemoryDatabaseBuilder(context, FitConnectDatabase::class.java)
                .allowMainThreadQueries()
        }
    }
}

/**
 * Awaits the value of a [LiveData] with a given timeout
 * @param timeoutMs Max timeout in milliseconds
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.awaitValue(timeoutMs: Long = 5000): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@awaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        if(!latch.await(timeoutMs, TimeUnit.MILLISECONDS))
            throw TimeoutException("LiveData was never set.")
    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}