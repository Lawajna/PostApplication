package com.example.posts.ui.screens.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.posts.PostApplication
import com.example.posts.data.AppRepository
import com.example.posts.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostDetailsViewModel(
    private val postId: Int,
    private val appRepository: AppRepository
) : ViewModel() {

    sealed interface UiState {
        object Loading: UiState
        data class Success(val post: Post) : UiState
        data class Error(val message: String) : UiState
    }
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchPost()
    }

    private fun fetchPost() {
        viewModelScope.launch {
            try {
                val post = appRepository.getPostById(postId)
                _uiState.value = UiState.Success(post)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Błąd: ${e.localizedMessage}")
            }
        }
    }
    companion object {
        fun Factory(postId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostApplication)
                val appRepository = application.container.repository
                PostDetailsViewModel(postId, appRepository)
            }
        }
    }
}
