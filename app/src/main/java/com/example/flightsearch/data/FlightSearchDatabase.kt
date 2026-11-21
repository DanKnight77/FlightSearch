package com.example.flightsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase

// Defines the Room database and the tables it contains.
@Database(
    entities = [Airport::class, Favorite::class],
    version = 1,
    exportSchema = false
)
abstract class FlightSearchDatabase : RoomDatabase() {

    // Provides access to airport database operations.
    abstract fun airportDao(): AirportDao

    // Provides access to favorite route operations.
    abstract fun favoriteDao(): FavoriteDao
}