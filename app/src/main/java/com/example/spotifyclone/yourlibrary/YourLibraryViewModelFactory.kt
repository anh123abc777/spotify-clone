package com.example.spotifyclone.yourlibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotifyclone.network.User

@Suppress("UNCHECKED_CAST")
class YourLibraryViewModelFactory
    (val token: String,
    val user: User
     ):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(YourLibraryViewModel::class.java))
            return YourLibraryViewModel(token,user) as T
        throw IllegalArgumentException("Unknown VM class")
    }
}