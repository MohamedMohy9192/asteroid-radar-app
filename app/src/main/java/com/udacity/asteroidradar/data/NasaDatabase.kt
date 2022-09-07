package com.udacity.asteroidradar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class NasaDatabase : RoomDatabase() {

    abstract fun getAsteroidDao(): AsteroidDao

    companion object {
        @Volatile
        private var INSTANCE: NasaDatabase? = null

        fun getNasaDatabase(context: Context): NasaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NasaDatabase::class.java,
                    "nasa_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}