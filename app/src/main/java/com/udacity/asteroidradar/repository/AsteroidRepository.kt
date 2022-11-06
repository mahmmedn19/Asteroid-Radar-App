package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.asAsteroidEntities
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DayProvider
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.ArrayList

class AsteroidRepository(private val database: AsteroidDatabase) {

    suspend fun refreshAsteroids(
        startDate: String = DayProvider.getToday(),
        endDate: String = DayProvider.getSevenDaysLater()
    ) {
        var asteroidList: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = AsteroidApi.retrofitService.getAsteroids(
                startDate, endDate,
                Constants.API_KEY
            ).await()
            asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))

            database.asteroidDao.insert(*asteroidList.asAsteroidEntities())
        }
    }

    suspend fun deletePreviousDayAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deletePreviousDayAsteroids(DayProvider.getToday())
        }
    }

}