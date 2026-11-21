package com.example.flightsearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightsearch.FlightSearchApplication

// Creates the FlightSearchViewModel with its required repositories.
class FlightSearchViewModelFactory(
    private val application: FlightSearchApplication
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(FlightSearchViewModel::class.java)) {

            // Provides the database and saved preferences repositories.
            @Suppress("UNCHECKED_CAST")
            return FlightSearchViewModel(
                application.repository,
                application.userPreferencesRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}