package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NasaApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getNextSevenDaysAsteroids(
        @Query("start_date") startDate: String = getNextSevenDaysFormattedDates().first(),
        @Query("end_date") endDate: String = getNextSevenDaysFormattedDates().last(),
        @Query("api_key") key: String = API_KEY
    ): String

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query("api_key") key: String): PictureOfDay
}

object NasaApi {
    val nasaService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }

}