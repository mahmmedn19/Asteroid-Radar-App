package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Deferred<ResponseBody>


    @GET("planetary/apod")
    fun getPictureOfDay(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Deferred<PictureOfDay>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}