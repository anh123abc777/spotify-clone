package com.example.spotifyclone.network

data class Tracks(
    val items: List<Track>?= listOf()
)

data class SavedTracks(
    val items: List<SavedTrack>? = listOf()
)

data class SavedTrack(
    val track: Track,
    val added_at: String
)

data class Track (
    val artists : List<Artist>? = listOf(),
    val disc_number : Int? = 0,
    val duration_ms : Long? = 0L,
    val explicit : Boolean? = false,
    val external_urls : External_urls? = External_urls(""),
    val href : String? = "",
    val id : String = "",
    val is_local : Boolean? = false,
    val is_playable : Boolean? = false,
    val name : String="",
    val preview_url : String? = "",
    val track_number : Int? = 0,
    val type : String = "",
    val uri : String = "",
    val album: Album? = Album()
)