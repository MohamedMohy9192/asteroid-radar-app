package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.AsteroidDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.ArrayList

class AsteroidRepository(
    private val asteroidDao: AsteroidDao
) {

    val asteroids: LiveData<List<Asteroid>> = asteroidDao.getAsteroids()

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