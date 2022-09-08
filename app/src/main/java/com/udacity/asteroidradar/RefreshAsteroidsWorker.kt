package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.NasaDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshAsteroidsWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        val database = NasaDatabase.getNasaDatabase(applicationContext)
        val repository = AsteroidRepository(database.getAsteroidDao())

        try {
            repository.refreshAsteroids()
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }
}