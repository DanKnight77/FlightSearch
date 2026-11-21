package com.example.flightsearch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.viewmodel.FlightSearchViewModel

// Main screen for searching airports and viewing favorite flights.
@Composable
fun FlightSearchScreen(
    viewModel: FlightSearchViewModel
) {
    // Observes information provided by the ViewModel.
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedAirport by viewModel.selectedAirport.collectAsState()
    val destinations by viewModel.destinations.collectAsState()
    val favoriteRoutes by viewModel.favoriteRoutes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // App title.
        Text(
            text = "Flight Search",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Airport search field.
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            label = { Text("Search airports") },
            placeholder = { Text("Enter airport name or IATA code") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Displays destinations after an airport is selected.
        if (selectedAirport != null) {

            Text(
                text = "Departing from ${selectedAirport!!.name} (${selectedAirport!!.iataCode})",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = { viewModel.clearSelectedAirport() }
            ) {
                Text("Change departure airport")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Available destinations",
                style = MaterialTheme.typography.titleLarge
            )

            LazyColumn {
                items(destinations) { airport ->
                    AirportCard(
                        airport = airport,
                        favoriteRoutes = favoriteRoutes,
                        onFavoriteClick = {
                            val existingFavorite = favoriteRoutes.firstOrNull {
                                it.departureCode == selectedAirport!!.iataCode &&
                                        it.destinationCode == airport.iataCode
                            }

                            if (existingFavorite == null) {
                                viewModel.addFavorite(
                                    selectedAirport!!.iataCode,
                                    airport.iataCode
                                )
                            } else {
                                viewModel.removeFavorite(existingFavorite)
                            }
                        }
                    )
                }
            }

            // Displays airport autocomplete suggestions.
        } else if (searchQuery.isNotBlank()) {

            Text(
                text = "Matching airports",
                style = MaterialTheme.typography.titleLarge
            )

            LazyColumn {
                items(searchResults) { airport ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = {
                            viewModel.selectAirport(airport)
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = airport.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = airport.iataCode,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Displays saved favorite routes when the search is empty.
        } else {

            Text(
                text = "Favorite Routes",
                style = MaterialTheme.typography.titleLarge
            )

            if (favoriteRoutes.isEmpty()) {
                Text(
                    text = "No favorite routes yet. Search for an airport to get started.",
                    modifier = Modifier.padding(top = 12.dp)
                )
            } else {
                LazyColumn {
                    items(favoriteRoutes) { favorite ->
                        FavoriteRouteCard(
                            favorite = favorite,
                            onRemove = {
                                viewModel.removeFavorite(favorite)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Displays an airport and its favorite button.
@Composable
fun AirportCard(
    airport: Airport,
    favoriteRoutes: List<Favorite>,
    onFavoriteClick: () -> Unit
) {
    val isFavorite = favoriteRoutes.any {
        it.destinationCode == airport.iataCode
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = airport.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = airport.iataCode,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(onClick = onFavoriteClick) {
                Text(if (isFavorite) "★ Saved" else "☆ Favorite")
            }
        }
    }
}

// Displays a saved favorite route.
@Composable
fun FavoriteRouteCard(
    favorite: Favorite,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${favorite.departureCode} → ${favorite.destinationCode}",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Favorite flight route",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(onClick = onRemove) {
                Text("Remove")
            }
        }
    }
}