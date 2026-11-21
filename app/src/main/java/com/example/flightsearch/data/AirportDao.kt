package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Defines the database operations for airports.
@Dao
interface AirportDao {

    // Searches airports by their name or three-letter code.
    @Query("""
        SELECT * FROM airport
        WHERE iata_code LIKE '%' || :searchQuery || '%'
        OR name LIKE '%' || :searchQuery || '%'
        ORDER BY passengers DESC
    """)
    fun searchAirports(searchQuery: String): Flow<List<Airport>>
    // Retrieves destination airports other than the departure airport.
    @Query("""
        SELECT * FROM airport
        WHERE iata_code != :departureCode
        ORDER BY passengers DESC
    """)
    fun getDestinations(departureCode: String): Flow<List<Airport>>
}