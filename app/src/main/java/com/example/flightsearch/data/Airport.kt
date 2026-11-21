package com.example.flightsearch.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents the airport table in the database.
@Entity(tableName = "airport")
data class Airport(

    // Unique ID for each airport.
    @PrimaryKey
    val id: Int,

    // Three-letter airport code.
    @ColumnInfo(name = "iata_code")
    val iataCode: String,

    // Full airport name.
    val name: String,

    // Number of passengers who use the airport.
    val passengers: Int
)