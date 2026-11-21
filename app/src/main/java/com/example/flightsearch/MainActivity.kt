package com.example.flightsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flightsearch.ui.theme.FlightSearchTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.ui.screens.FlightSearchScreen
import com.example.flightsearch.ui.viewmodel.FlightSearchViewModel
import com.example.flightsearch.ui.viewmodel.FlightSearchViewModelFactory

// Main activity that launches the Flight Search application.
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // Gets the application and its repositories.
            val application = LocalContext.current.applicationContext
                    as FlightSearchApplication

            // Creates the ViewModel using our custom factory.
            val flightSearchViewModel: FlightSearchViewModel = viewModel(
                factory = FlightSearchViewModelFactory(application)
            )

            // Applies the app's theme.
            FlightSearchTheme {

                // Displays the Flight Search screen.
                FlightSearchScreen(
                    viewModel = flightSearchViewModel
                )
            }
        }
    }
}