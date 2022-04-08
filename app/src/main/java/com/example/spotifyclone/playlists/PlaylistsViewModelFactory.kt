package com.example.spotifyclone.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spotifyclone.network.PlaylistInfo
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class PlaylistsViewModelFactory
    (val token: String,
     private val playlistInfo: PlaylistInfo
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlaylistsViewModel::class.java))
            return PlaylistsViewModel(token,playlistInfo) as T
        throw IllegalArgumentException("Unknown VM class")
    }
}