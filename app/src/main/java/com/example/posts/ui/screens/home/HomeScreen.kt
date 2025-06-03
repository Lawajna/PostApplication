            package com.example.posts.ui.screens.home

            import androidx.compose.foundation.clickable
            import androidx.compose.foundation.layout.Box
            import androidx.compose.foundation.layout.Column
            import androidx.compose.foundation.layout.Spacer
            import androidx.compose.foundation.layout.fillMaxSize
            import androidx.compose.foundation.layout.fillMaxWidth
            import androidx.compose.foundation.layout.height
            import androidx.compose.foundation.layout.padding
            import androidx.compose.foundation.lazy.LazyColumn
            import androidx.compose.foundation.lazy.items
            import androidx.compose.material.icons.Icons
            import androidx.compose.material.icons.filled.AccountCircle
            import androidx.compose.material3.Card
            import androidx.compose.material3.CardDefaults
            import androidx.compose.material3.CircularProgressIndicator
            import androidx.compose.material3.ExperimentalMaterial3Api
            import androidx.compose.material3.HorizontalDivider
            import androidx.compose.material3.IconButton
            import androidx.compose.material3.MaterialTheme
            import androidx.compose.material3.Scaffold
            import androidx.compose.material3.Text
            import androidx.compose.material3.TopAppBar
            import androidx.compose.runtime.Composable
            import androidx.compose.runtime.collectAsState
            import androidx.compose.runtime.getValue
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.unit.dp
            import androidx.navigation.NavController
            import com.example.posts.model.Post
            import com.example.posts.model.PostDetails
            import com.example.posts.model.User
            import com.example.posts.model.UserDetails
            import androidx.compose.material3.Icon
            import com.example.posts.model.Profile
            import androidx.compose.material3.TopAppBarDefaults

            @OptIn(ExperimentalMaterial3Api::class)
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
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }

                        is HomeViewModel.UiState.Error -> {
                            val msg = (state as HomeViewModel.UiState.Error).message
                            Box(
                                Modifier.fillMaxSize().padding(paddingValues),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                Text(msg)
                            }
                        }

                        is HomeViewModel.UiState.Success -> {
                            val posts = (state as HomeViewModel.UiState.Success).posts
                            val users = (state as HomeViewModel.UiState.Success).users.associateBy { it.id }

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                                    .padding(horizontal = 16.dp)
                            ) {
                                items(posts) { post ->
                                    val user = users[post.userId]
                                    PostItem(post, user, navigationController)
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
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
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
            fun PostItem(post: Post, user: User?, navController: NavController) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable { navController.navigate(PostDetails(post.id)) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        Modifier
                            .padding(16.dp)
                    ) {
                        user?.let {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.clickable {
                                    navController.navigate(UserDetails(it.id))
                                }
                            )
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
