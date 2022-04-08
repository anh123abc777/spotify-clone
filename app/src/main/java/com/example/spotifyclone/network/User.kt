package com.example.spotifyclone.network

import com.example.spotifyclone.network.Image

data class User(
    val country: String = "",
    val display_name: String = "",
    val id: String = "",
    val images: List<Image> = listOf(),
    val product: String = "",
    val type: String = "user",
    val uri: String = "",
    val email :  String = "",
    )