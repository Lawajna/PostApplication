package com.example.posts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.posts.model.Home
import com.example.posts.model.PostDetails
import com.example.posts.ui.screens.home.HomeScreen
import com.example.posts.ui.screens.home.HomeViewModel
import com.example.posts.ui.theme.PostsTheme
import com.example.posts.model.Profile
import com.example.posts.model.UserDetails
import com.example.posts.ui.screens.post.PostDetailsScreen
import com.example.posts.ui.screens.post.PostDetailsViewModel
import com.example.posts.ui.screens.profile.ProfileViewModel
import com.example.posts.ui.screens.profile.ProfileScreen
import com.example.posts.ui.screens.user.UserDetailsScreen
import com.example.posts.ui.screens.user.UserDetailsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PostsTheme {
                val navigationController = rememberNavController()

                NavHost(navController = navigationController, startDestination = Home) {
                    composable<Home> {
                        val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
                        HomeScreen(
                            viewModel = viewModel,
                            navigationController = navigationController
                        )
                    }

                    composable<Profile> {
                        val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
                        ProfileScreen(
                            navController = navigationController,
                            viewModel = viewModel
                        )
                    }

                    composable<PostDetails> {
                        val args = it.toRoute<PostDetails>()
                        val viewModel: PostDetailsViewModel = viewModel(factory = PostDetailsViewModel.Factory(args.postId))
                        PostDetailsScreen(
                            navigationController = navigationController,
                            viewModel = viewModel
                        )
                    }

                    composable<UserDetails> {
                        val args = it.toRoute<UserDetails>()
                        val viewModel: UserDetailsViewModel = viewModel(factory = UserDetailsViewModel.Factory(args.userId))
                        UserDetailsScreen(
                            navigationController = navigationController,
                            viewModel = viewModel
                        )
                    }
            }
        }
    }
}
}

