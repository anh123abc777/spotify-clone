package com.example.spotifyclone.network

import com.example.tonezone.R


data class Playlist(
    val collaborative: Boolean?=false,
    val id : String,
    val description: String,
    val href: String,
    val images: List<Image>?=listOf(Image(null,R.drawable.ic_baseline_home_24.toString(),null)),
    val name: String,
    val owner: Owner,
    val primary_color: Int?=null,
    val public: Boolean?,
    val type: String,
    val uri: String,
    val tracks: Tracks?= Tracks(),
)
