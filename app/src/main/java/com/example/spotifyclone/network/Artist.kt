package com.example.spotifyclone.network


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ArtistsObject(
	val artists: Artists?= Artists()
)

@Parcelize
data class Artists(
	val items: List<Artist>?= listOf()
) : Parcelable

@Parcelize
data class Artist (
	val href : String?="",
	val id : String?="",
	val name : String?="",
	val type : String?="",
	val uri : String?="",
	val followers: Follower?= Follower(),
	val genres: List<String>?= listOf(),
	val images: List<Image>?= listOf(),
	val popularity: Int?=0,
) : Parcelable

@Parcelize
data class Follower(
	val href: String?="",
	val total: Long?=0L
) : Parcelable