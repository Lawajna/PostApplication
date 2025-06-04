package com.example.posts.data

import com.example.posts.data.network.ApiService
import com.example.posts.model.Post
import com.example.posts.model.Todo
import com.example.posts.model.User

class AppRepository(private val api: ApiService) {

    suspend fun getPosts(): List<Post> = api.getPosts()

    suspend fun getUsers(): List<User> = api.getUsers()

    suspend fun getPostById(postId: Int): Post = api.getPostById(postId)

    suspend fun getUserById(userId: Int): User = api.getUserById(userId)

    suspend fun getTodosByUserId(userId: Int): List<Todo> = api.getTodosByUserId(userId)
}