package com.example.posts.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.posts.PostApplication
import com.example.posts.data.AppRepository
import com.example.posts.data.UserPreferences
import com.example.posts.model.Post
import com.example.posts.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine

class HomeViewModel(
    private val appRepository: AppRepository,
    private val prefsManager: UserPreferences
) : ViewModel() {
    sealed interface UiState {
        data object Loading: UiState
        data class Success(val posts: List<Post>, val users: List<User>, val firstName: String, val lastName: String) : UiState
        data class Error(val message: String) : UiState
    }
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchData()
    }
    private fun fetchData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val posts = appRepository.getPosts()
                val users = appRepository.getUsers()

                combine(
                    prefsManager.userFirstName,
                    prefsManager.userLastName
                ){ first, last ->

                    UiState.Success(posts, users, first, last)
                }.collect { combinedState ->
                    _uiState.value = combinedState }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Błąd: ${e.localizedMessage}")
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostApplication)
                val appRepository = application.container.repository
                val prefs = application.container.userPreferences
                HomeViewModel(appRepository, prefs)
            }
        }
    }
}