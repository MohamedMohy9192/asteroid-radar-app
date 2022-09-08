package com.udacity.asteroidradar.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroids ORDER BY close_approach_date ASC")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date == :date")
    fun getTodayAsteroids(date: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date >= :date ORDER BY close_approach_date ASC")
    fun getWeekAsteroids(date: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroids(asteroids: List<Asteroid>)
}