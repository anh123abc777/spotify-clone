package com.example.spotifyclone.network

data class PlaylistsObject(
    val playlists: Playlists
)

data class Playlists(
    val items: List<Playlist>?= listOf(),
)
