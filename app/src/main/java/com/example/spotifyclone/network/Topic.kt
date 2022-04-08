package com.example.spotifyclone.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic (
    var genres: List<String>) : Parcelable