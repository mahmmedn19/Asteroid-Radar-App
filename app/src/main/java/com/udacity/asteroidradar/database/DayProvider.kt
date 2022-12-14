package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

object DayProvider {

    fun getToday():String{
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT,Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun getSevenDaysLater(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }
}