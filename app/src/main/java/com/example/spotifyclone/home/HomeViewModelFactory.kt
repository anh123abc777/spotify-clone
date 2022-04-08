package com.example.spotifyclone.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotifyclone.network.User
import java.lang.IllegalArgumentException

class HomeViewModelFactory(
    private val token: String,
    private val user: User
    ): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(token,user) as T
        throw IllegalArgumentException("Unknown VM class")
    }

}