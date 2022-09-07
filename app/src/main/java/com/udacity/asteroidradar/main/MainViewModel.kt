package com.udacity.asteroidradar.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {

    val asteroids = MutableLiveData<List<Asteroid>>()

    init {
        getAsteroids()
    }

    private fun getAsteroids() = viewModelScope.launch {
        try {
            val response = NasaApi.nasaService.getNextSevenDaysAsteroids()

            val asteroidList = JSONObject(response)
            asteroids.value = parseAsteroidsJsonResult(asteroidList)
        } catch (e: Exception) {

        }
    }
}