package com.example.posts

import android.app.Application
import com.example.posts.data.AppContainer
import com.example.posts.data.DefaultAppContainer

class   PostApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}