package com.example.spotifyclone.network

import com.example.spotifyclone.network.Albums
import com.example.spotifyclone.network.Artists
import com.example.spotifyclone.network.Playlists
import com.example.spotifyclone.network.Tracks

data class SearchedItem(
    val playlists: Playlists?= Playlists(),
    val artists: Artists?= Artists(),
    val tracks: Tracks?= Tracks(),
    val albums: Albums?= Albums(listOf())
)
