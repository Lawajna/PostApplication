package com.example.posts.model

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Profile

@Serializable
data class PostDetails(val postId: Int)

@Serializable
data class UserDetails(val userId: Int)

