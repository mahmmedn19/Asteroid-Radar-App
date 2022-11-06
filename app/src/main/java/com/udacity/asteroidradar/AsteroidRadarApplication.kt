package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.DataWork
import com.udacity.asteroidradar.work.DeleteDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRefreshRequest = PeriodicWorkRequestBuilder<DataWork>(
            // once a day
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "AsteroidRefreshDataWorker",
            ExistingPeriodicWorkPolicy.KEEP, // keep - will disregard the new request
            repeatingRefreshRequest
        )

        val repeatingDeleteRequest = PeriodicWorkRequestBuilder<DeleteDataWork>(
            // once a day
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "AsteroidDeleteDataWorker",
            ExistingPeriodicWorkPolicy.KEEP, // keep - will disregard the new request
            repeatingDeleteRequest
        )
    }
}