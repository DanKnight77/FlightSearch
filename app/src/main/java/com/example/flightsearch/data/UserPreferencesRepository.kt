package com.example.flightsearch.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Creates a DataStore instance for saving user preferences.
private val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)

// Manages the user's saved search preferences.
class UserPreferencesRepository(
    private val context: Context
) {

    // Key used to store the last airport search.
    private val searchQueryKey = stringPreferencesKey("search_query")

    // Retrieves the saved search text.
    val searchQuery: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[searchQueryKey] ?: ""
        }

    // Saves the latest search text.
    suspend fun saveSearchQuery(searchQuery: String) {
        context.dataStore.edit { preferences ->
            preferences[searchQueryKey] = searchQuery
        }
    }
}