package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.utils.Constants

@Database(entities = [Asteroid::class], version = 5, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    // abstract fun pictureOfDay():PictureDao

    companion object {
        @Volatile
        private var INSTANCE: AsteroidDatabase? = null
        fun getDatabase(context: Context): AsteroidDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}