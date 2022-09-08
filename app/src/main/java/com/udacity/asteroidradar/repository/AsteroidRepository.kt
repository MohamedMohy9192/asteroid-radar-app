package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.AsteroidDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

enum class AsteroidsFilter {
    SHOW_WEEK_ASTEROIDS, SHOW_TODAY_ASTEROIDS, SHOW_SAVED_ASTEROIDS
}

class AsteroidRepository(
    private val asteroidDao: AsteroidDao
) {

    fun getAsteroids(filter: AsteroidsFilter): LiveData<List<Asteroid>> {
        return when (filter) {
            AsteroidsFilter.SHOW_SAVED_ASTEROIDS -> asteroidDao.getAsteroids()
            AsteroidsFilter.SHOW_TODAY_ASTEROIDS -> asteroidDao.getTodayAsteroids(
                getNextSevenDaysFormattedDates().first()
            )
            AsteroidsFilter.SHOW_WEEK_ASTEROIDS -> asteroidDao.getWeekAsteroids(
                getNextSevenDaysFormattedDates().first()
            )
        }
    }

    suspend fun getPictureOfDay() = NasaApi.nasaService.getImageOfTheDay()

    suspend fun refreshAsteroids() {
        val response = NasaApi.nasaService.getNextSevenDaysAsteroids()
        val asteroidList = parseAsteroidsJson(response)
        insertAsteroids(asteroidList)
    }

    private suspend fun insertAsteroids(asteroids: List<Asteroid>) {
        asteroidDao.insertAsteroids(asteroids)
    }

    private suspend fun parseAsteroidsJson(asteroidsJsonResponse: String): List<Asteroid> {
        var asteroidList: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidsJsonResponse))
        }
        return asteroidList
    }
}