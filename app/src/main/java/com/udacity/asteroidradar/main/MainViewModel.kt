package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DayProvider
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment: LiveData<Asteroid?>
        get() = _navigateToDetailFragment

    private val _asteroid = MutableLiveData<List<Asteroid>>()
    val asteroid: LiveData<List<Asteroid>>
        get() = _asteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay


    init {
        onViewWeekAsteroidsClicked()
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
                refreshPictureOfDay()
              //  Log.d("MainViewModel",asteroidRepository.refreshAsteroids().toString())
            } catch (e: Exception) {
                println("Exception refreshing data: $e.message")
                // _display Snackbar Event.value = true
            }
        }
    }
    private suspend fun getPictureOfDay(): PictureOfDay? {
        var pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfDay = AsteroidApi.retrofitService.getPictureOfDay().await()
        }
        if (pictureOfDay.mediaType == "image") {
            return pictureOfDay
        }
        return null
    }

    private suspend fun refreshPictureOfDay() {
        _pictureOfDay.value = getPictureOfDay()
    }



    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun doneNavigating() {
        _navigateToDetailFragment.value = null
    }

    fun onViewWeekAsteroidsClicked() {
        viewModelScope.launch {
            database.asteroidDao
                .getAsteroidsThisWeek(DayProvider.getToday(), DayProvider.getSevenDaysLater())
                .collect { asteroids ->
                    _asteroid.value = asteroids
                }
        }
    }

    fun onTodayAsteroidsClicked() {
        viewModelScope.launch {
            database.asteroidDao
                .getAsteroidsThisWeek(DayProvider.getToday(), DayProvider.getToday())
                .collect { asteroids ->
                    _asteroid.value = asteroids
                }
        }
    }

    fun onSavedAsteroidsClicked() {
        viewModelScope.launch {
            database.asteroidDao.getAsteroids().collect { asteroids ->
                _asteroid.value = asteroids
            }
        }
    }
}
