package com.udacity.asteroidradar.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroids ORDER BY close_approach_date ASC")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Insert
    suspend fun insertAsteroids(asteroids: List<Asteroid>)
}