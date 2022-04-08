package com.example.spotifyclone.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotifyclone.network.*
import kotlinx.coroutines.*

class HomeViewModel(val token: String, val user: User) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob+ Dispatchers.Main)

    private var _groupPlaylists = MutableLiveData<List<GroupPlaylist>>()
    val groupPlaylists : LiveData<List<GroupPlaylist>>
        get() = _groupPlaylists

    private val _navigateToPlaylistDetails = MutableLiveData<PlaylistInfo>()
    val navigateToPlaylistDetails : LiveData<PlaylistInfo>
        get() = _navigateToPlaylistDetails

    init {
        getGroupPlaylistsData()
    }

    private fun getGroupPlaylistsData() {
        uiScope.launch {

            _groupPlaylists.value = try {
                val featuredPlaylists = ToneApi.retrofitService
                    .getFeaturedPlaylistsAsync("Bearer $token").playlists.items

                val charts = ToneApi.retrofitService
                    .getChartsAsync("Bearer $token").playlists.items

                val albumReleases = ToneApi.retrofitService
                    .getNewAlbumReleases("Bearer $token").albums.items

                listOf(
                    GroupPlaylist("feature playlist", featuredPlaylists),
                    GroupPlaylist("charts", charts),
                    GroupPlaylist("new releases",convertAlbumsToPlaylists((albumReleases)))
                )

            } catch (e: Exception) {
                listOf()
            }
        }
    }

    private fun convertAlbumsToPlaylists(albums: List<Album>): List<Playlist> =
        albums.map {
            Playlist(
                id = it.id!!,
                description = it.album_group!!,
                href = "",
                images = it.images,
                name = it.name!!,
                owner = Owner(),
                public = false,
                type = it.type!!,
                uri = it.uri!!
            )
        }


     fun displayPlaylistDetails(playlistInfo: PlaylistInfo){
        _navigateToPlaylistDetails.value = playlistInfo
    }

    @SuppressLint("NullSafeMutableLiveData")
     fun displayPlaylistDetailsComplete(){
        _navigateToPlaylistDetails.value = null
    }

    override fun onCleared() {
        uiScope.cancel()
        super.onCleared()
    }
}