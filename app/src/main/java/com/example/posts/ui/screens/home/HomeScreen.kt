package com.example.posts.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.posts.model.Post
import com.example.posts.model.PostDetails
import com.example.posts.model.User
import com.example.posts.model.UserDetails
import com.example.posts.model.Profile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigationController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            if (state is HomeViewModel.UiState.Success) {
                val s = state as HomeViewModel.UiState.Success
                CustomTopBar(
                    titleText = "Witaj, ${s.firstName} ${s.lastName}",
                    onProfileClick = { navigationController.navigate(Profile) }
                )
            }
        }
    ) { paddingValues ->
        when (state) {
            is HomeViewModel.UiState.Loading -> Box(
                Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is HomeViewModel.UiState.Error -> {
                val msg = (state as HomeViewModel.UiState.Error).message
                Box(
                    Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(msg)
                }
            }

            is HomeViewModel.UiState.Success -> {
                val successState = state as HomeViewModel.UiState.Success
                val posts = successState.posts
                val users = successState.users.associateBy { it.id }
                val filter = successState.filter
                val favoritePostIds = successState.favoritePostIds

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Spacer(modifier = Modifier.height(2.dp))
                            TextField(
                                value = filter,
                                onValueChange = { viewModel.onFilterChange(it) },
                                placeholder = { Text("np. Filtruj po nazwie użytkownika") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    val filteredPosts = posts.filter { post ->
                        val userName = users[post.userId]?.name?.lowercase() ?: ""
                        userName.contains(filter.lowercase())
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredPosts) { post ->
                            val user = users[post.userId]
                            PostItem(
                                post = post,
                                user = user,
                                navController = navigationController,
                                isFavorite = favoritePostIds.contains(post.id),
                                onFavoriteToggle = { viewModel.toggleFavorite(post) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    titleText: String,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        },
        actions = {
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profil",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PostItem(
    post: Post,
    user: User?,
    navController: NavController,
    isFavorite: Boolean,
    onFavoriteToggle: (Post) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { navController.navigate(PostDetails(post.id)) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                user?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate(UserDetails(it.id))
                            }
                    )
                }
                IconToggleButton(
                    checked = isFavorite,
                    onCheckedChange = { onFavoriteToggle(post) }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Usuń z ulubionych" else "Dodaj do ulubionych",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                post.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        }
    }
}
