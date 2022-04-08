package com.example.spotifyclone.yourplaylist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class YourPlaylistViewModel(val token: String, val trackID: String): ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    fun addItemToPlaylist(){
        uiScope.launch {

        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}