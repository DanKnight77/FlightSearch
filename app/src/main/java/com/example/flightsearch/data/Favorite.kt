package com.example.flightsearch.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a saved favorite flight route.
@Entity(tableName = "favorite")
data class Favorite(

    // Unique ID for each favorite route.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Airport code where the flight starts.
    @ColumnInfo(name = "departure_code")
    val departureCode: String,

    // Airport code where the flight ends.
    @ColumnInfo(name = "destination_code")
    val destinationCode: String
)