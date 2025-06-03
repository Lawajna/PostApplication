    package com.example.posts.data

import android.content.Context
import android.net.Uri
import java.util.*

fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    val fileName = UUID.randomUUID().toString() + ".jpg"
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
    inputStream?.use { input ->
        outputStream.use { output -> input.copyTo(output) }
    }
    return fileName
}