package com.example.spotifyclone.playlistdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotifyclone.network.PlaylistInfo
import com.example.spotifyclone.network.User

@Suppress("UNCHECKED_CAST")
class PlaylistDetailsViewModelFactory
    (val token: String,
     val playlistInfo: PlaylistInfo,
     private val userProfile: User
     ): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlaylistDetailsViewModel::class.java))
            return PlaylistDetailsViewModel(token,playlistInfo,userProfile) as T
        throw IllegalArgumentException("Unknown VM class")
    }
}