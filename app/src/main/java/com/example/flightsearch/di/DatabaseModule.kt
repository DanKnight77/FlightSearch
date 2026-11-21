package com.example.flightsearch.di

import android.content.Context
import androidx.room.Room
import com.example.flightsearch.data.FlightSearchDatabase

// Provides the Room database for the application.
object DatabaseModule {

    // Creates the database using the provided context.
    fun provideDatabase(context: Context): FlightSearchDatabase {
        return Room.databaseBuilder(
            context,
            FlightSearchDatabase::class.java,
            "flight_search_database"
        )
            // Opens the instructor-provided database from assets.
            .createFromAsset("database/flight_search.db")
            .build()
    }
}

