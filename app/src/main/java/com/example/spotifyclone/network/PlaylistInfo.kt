package com.example.spotifyclone.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PlaylistInfo(
    val id: String,
    val name: String,
    val description: String,
    var image: String?,
    val uri: String,
    val type: String,
) : Parcelable