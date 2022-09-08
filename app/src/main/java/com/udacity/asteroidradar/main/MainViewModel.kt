package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.AsteroidsFilter
import kotlinx.coroutines.launch

class MainViewModel(private val asteroidRepository: AsteroidRepository) : ViewModel() {

    private val _filter = MutableLiveData(AsteroidsFilter.SHOW_SAVED_ASTEROIDS)

    val asteroids: LiveData<List<Asteroid>> =
        _filter.switchMap {
            asteroidRepository.getAsteroids(it)
        }

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?> get() = _navigateToSelectedAsteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> = _pictureOfDay

    init {
        getAsteroids()
        getPictureOfDay()
    }

    private fun getPictureOfDay() = viewModelScope.launch {
        try {
            _pictureOfDay.value = asteroidRepository.getPictureOfDay()
        } catch (e: Exception) {
            Log.e("MainViewModel", e.message, e)
        }
    }

    private fun getAsteroids() = viewModelScope.launch {
        try {
            asteroidRepository.refreshAsteroids()
        } catch (e: Exception) {
            Log.e("MainViewModel", e.message, e)
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    fun updateAsteroidsFilter(filter: AsteroidsFilter) {
        _filter.value = filter
    }
}

class AsteroidViewModelFactory(private val asteroidRepository: AsteroidRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(asteroidRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}