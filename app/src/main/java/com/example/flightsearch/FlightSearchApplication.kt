package com.example.flightsearch

import android.app.Application
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.data.repository.FlightSearchRepository
import com.example.flightsearch.di.DatabaseModule

// Initializes the database and repositories for the application.
class FlightSearchApplication : Application() {

    // Creates the database when the application starts.
    private val database by lazy {
        DatabaseModule.provideDatabase(this)
    }

    // Creates the repository used by the rest of the app.
    val repository: FlightSearchRepository by lazy {
        FlightSearchRepository(
            database.airportDao(),
            database.favoriteDao()
        )
    }

    // Creates the repository that manages saved search preferences.
    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(this)
    }
}