package com.example.spotifyclone.network

data class SpotifyData (

	val href : String,
	val items : List<Track>,
	val limit : Int,
	val next : String?="",
	val offset : Int,
	val previous : String?="",
	val total : Int?=0
)