package com.example.spotifyclone.playlists

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotifyclone.network.Playlist
import com.example.spotifyclone.network.PlaylistInfo
import com.example.spotifyclone.network.ToneApi
import kotlinx.coroutines.*

class PlaylistsViewModel(val token: String, val playlistInfo: PlaylistInfo) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _categoryPlaylists = MutableLiveData<List<Playlist>>()
    val categoryPlaylists : LiveData<List<Playlist>>
        get() = _categoryPlaylists

    init {
        if(playlistInfo.type=="genre") {
            getSearchResultData()
        }
    }

    private fun getSearchResultData() {
        uiScope.launch {
            _categoryPlaylists.value = try {
               ToneApi.retrofitService
                    .getCategoryPlaylistsAsync("Bearer $token", playlistInfo.id).playlists.items

            } catch (e: Exception) {
                Log.i("result", e.message!!)
                listOf()
            }
        }
    }

}