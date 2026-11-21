package com.example.flightsearch.data.repository

import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportDao
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteDao
import kotlinx.coroutines.flow.Flow

// Connects the ViewModel to the database.
class FlightSearchRepository(
    private val airportDao: AirportDao,
    private val favoriteDao: FavoriteDao
) {

    // Searches for airports using the user's search text.
    fun searchAirports(searchQuery: String): Flow<List<Airport>> {
        return airportDao.searchAirports(searchQuery)
    }
    // Retrieves available destinations for a departure airport.
    fun getDestinations(departureCode: String): Flow<List<Airport>> {
        return airportDao.getDestinations(departureCode)
    }

    // Retrieves all saved favorite routes.
    fun getAllFavorites(): Flow<List<Favorite>> {
        return favoriteDao.getAllFavorites()
    }

    // Saves a flight route as a favorite.
    suspend fun insertFavorite(favorite: Favorite) {
        favoriteDao.insertFavorite(favorite)
    }

    // Removes a flight route from favorites.
    suspend fun deleteFavorite(favorite: Favorite) {
        favoriteDao.deleteFavorite(favorite)
    }
}