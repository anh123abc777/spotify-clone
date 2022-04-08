package com.example.spotifyclone.network


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class External_urls (

	val spotify : String
) : Parcelable