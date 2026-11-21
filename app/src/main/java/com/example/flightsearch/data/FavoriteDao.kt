package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Defines database operations for favorite routes.
@Dao
interface FavoriteDao {

    // Retrieves all saved favorite routes.
    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<Favorite>>

    // Saves a new favorite route.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    // Removes a saved favorite route.
    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}