package com.example.posts.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.posts.PostApplication
import com.example.posts.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(private val preferences: UserPreferences) : ViewModel() {

    val firstName = preferences.userFirstName.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val lastName = preferences.userLastName.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val imagePath = preferences.imagePath.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    fun saveUser(first: String, last: String) {
        viewModelScope.launch {
            preferences.saveUserDetails(first, last)
        }
    }

    fun saveImagePath(path: String) {
        viewModelScope.launch {
            preferences.saveImagePath(path)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostApplication)
                val preferences = application.container.userPreferences
                ProfileViewModel(preferences)
            }
        }
    }
}