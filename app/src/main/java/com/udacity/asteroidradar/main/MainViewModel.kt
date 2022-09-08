package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class AsteroidStatus { LOADING, ERROR, DONE }

class MainViewModel(private val asteroidRepository: AsteroidRepository) : ViewModel() {

    private val _status = MutableLiveData<AsteroidStatus>()
    val status: LiveData<AsteroidStatus> get() = _status

    val asteroids: LiveData<List<Asteroid>> = asteroidRepository.asteroids

    init {
        getAsteroids()
    }

    private fun getAsteroids() = viewModelScope.launch {

        _status.value = AsteroidStatus.LOADING
        try {
            asteroidRepository.refreshAsteroids()
            _status.value = AsteroidStatus.DONE
        } catch (e: Exception) {
            _status.value = AsteroidStatus.ERROR
        }
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