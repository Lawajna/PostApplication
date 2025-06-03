package com.example.posts.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val FIRST_NAME_KEY = stringPreferencesKey("first_name")
        val LAST_NAME_KEY = stringPreferencesKey("last_name")
        val IMAGE_PATH_KEY = stringPreferencesKey("image_path")
    }

    suspend fun saveUserDetails(firstName: String, lastName: String) {
        context.dataStore.edit { prefs ->
            prefs[FIRST_NAME_KEY] = firstName
            prefs[LAST_NAME_KEY] = lastName
        }
    }

    suspend fun saveImagePath(path: String) {
        context.dataStore.edit { prefs ->
            prefs[IMAGE_PATH_KEY] = path
        }
    }

    val userFirstName: Flow<String> = context.dataStore.data.map { it[FIRST_NAME_KEY] ?: "" }
    val userLastName: Flow<String> = context.dataStore.data.map { it[LAST_NAME_KEY] ?: "" }
    val imagePath: Flow<String> = context.dataStore.data.map { it[IMAGE_PATH_KEY] ?: "" }
}