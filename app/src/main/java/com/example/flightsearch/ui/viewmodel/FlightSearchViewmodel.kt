package com.example.flightsearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.repository.FlightSearchRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.flatMapLatest

// Manages airport searches and favorite flight routes.
class FlightSearchViewModel(
    private val repository: FlightSearchRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    // Holds the text entered into the airport search field.
    private val _searchQuery = MutableStateFlow("")

    // Makes the search text available to the UI.
    val searchQuery = _searchQuery.asStateFlow()

    // Searches the database as the user types.
    @OptIn(FlowPreview::class)
    val searchResults: StateFlow<List<Airport>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                kotlinx.coroutines.flow.flowOf(emptyList())
            } else {
                repository.searchAirports(query)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
    // Updates the search text when the user types.
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query

        // Saves the latest search text.
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(query)
        }
    }

    // Restores the previous search when the app opens.
    init {
        viewModelScope.launch {
            userPreferencesRepository.searchQuery.collect { savedQuery ->
                _searchQuery.value = savedQuery
            }
        }
    }
    // Observes all saved favorite flight routes.
    val favoriteRoutes: StateFlow<List<Favorite>> =
        repository.getAllFavorites().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    // Saves a flight route to favorites.
    fun addFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            repository.insertFavorite(
                Favorite(
                    departureCode = departureCode,
                    destinationCode = destinationCode
                )
            )
        }
    }

    // Removes a flight route from favorites.
    fun removeFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }
    // Stores the airport selected by the user.
    private val _selectedAirport = MutableStateFlow<Airport?>(null)

    // Makes the selected airport available to the UI.
    val selectedAirport = _selectedAirport.asStateFlow()

    // Updates the selected departure airport.
    fun selectAirport(airport: Airport) {
        _selectedAirport.value = airport
    }

    // Clears the selected departure airport.
    fun clearSelectedAirport() {
        _selectedAirport.value = null
    }
    // Retrieves destinations for the selected departure airport.
    val destinations: StateFlow<List<Airport>> = selectedAirport
        .flatMapLatest { airport ->
            if (airport == null) {
                kotlinx.coroutines.flow.flowOf(emptyList())
            } else {
                repository.getDestinations(airport.iataCode)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
}