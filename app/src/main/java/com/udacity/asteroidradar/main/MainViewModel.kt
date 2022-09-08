package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(private val asteroidRepository: AsteroidRepository) : ViewModel() {

    val asteroids: LiveData<List<Asteroid>> = asteroidRepository.asteroids

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?> get() = _navigateToSelectedAsteroid

    init {
        getAsteroids()
    }

    private fun getAsteroids() = viewModelScope.launch {
        try {
            asteroidRepository.refreshAsteroids()
        } catch (e: Exception) {

        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
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