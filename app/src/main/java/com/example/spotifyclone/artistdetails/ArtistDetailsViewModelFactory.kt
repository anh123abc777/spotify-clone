package com.example.spotifyclone.artistdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotifyclone.network.PlaylistInfo
import com.example.spotifyclone.network.User

@Suppress("UNCHECKED_CAST")
class ArtistDetailsViewModelFactory(
    val token: String,
    val playlistInfo: PlaylistInfo,
    val user: User
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ArtistDetailsViewModel::class.java))
            return ArtistDetailsViewModel(token,playlistInfo,user) as T
        throw IllegalArgumentException("Unknown VM class")
    }
}