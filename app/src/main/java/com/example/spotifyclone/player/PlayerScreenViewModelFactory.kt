package com.example.spotifyclone.player

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlayerScreenViewModelFactory
    (private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlayerScreenViewModel::class.java))
            return PlayerScreenViewModel(application) as T
        throw IllegalArgumentException("Unknown VM class")
    }
}