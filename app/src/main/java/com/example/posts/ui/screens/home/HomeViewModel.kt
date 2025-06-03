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
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val appRepository: AppRepository,
    private val prefsManager: UserPreferences
) : ViewModel() {
    sealed interface UiState {
        data object Loading: UiState
        data class Success(val posts: List<Post>, val users: List<User>, val firstName: String, val lastName: String, val filter: String = "", val favoritePostIds: Set<Int> = emptySet() ) : UiState
        data class Error(val message: String) : UiState
    }
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    private val _favoritePostIds = MutableStateFlow<Set<Int>>(emptySet())

    val uiState: StateFlow<UiState> = combine(
        _uiState,
        _favoritePostIds
    ) { state, favorites ->
        if (state is UiState.Success) {
            state.copy(favoritePostIds = favorites)
        } else state
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, UiState.Loading)

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

    fun onFilterChange(newFilter: String) {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = currentState.copy(filter = newFilter)
        }
    }

    fun toggleFavorite(post: Post) {
        val current = _favoritePostIds.value.toMutableSet()
        if (current.contains(post.id)) {
            current.remove(post.id)
        } else {
            current.add(post.id)
        }
        _favoritePostIds.value = current
    }
}
