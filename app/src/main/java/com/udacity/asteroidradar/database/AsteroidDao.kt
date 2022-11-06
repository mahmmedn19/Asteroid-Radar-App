package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(vararg asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsThisWeek(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate ASC")
     fun getAsteroids(): Flow<List<Asteroid>>


    @Query("DELETE FROM asteroid_table WHERE closeApproachDate < :today")
    fun deletePreviousDayAsteroids(today: String): Int
}