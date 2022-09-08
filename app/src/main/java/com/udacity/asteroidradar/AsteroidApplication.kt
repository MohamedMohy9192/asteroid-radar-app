package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.data.NasaDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository

class AsteroidApplication : Application() {

    private val nasaDatabase: NasaDatabase by lazy { NasaDatabase.getNasaDatabase(this) }
    val repository: AsteroidRepository by lazy { AsteroidRepository(nasaDatabase.getAsteroidDao()) }
}