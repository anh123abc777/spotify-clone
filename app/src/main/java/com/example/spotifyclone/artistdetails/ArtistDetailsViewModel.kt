package com.example.spotifyclone.artistdetails

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotifyclone.adapter.LibraryAdapter
import com.example.spotifyclone.network.*
import kotlinx.coroutines.*

class ArtistDetailsViewModel(
    val token: String,
    val playlistInfo: PlaylistInfo,
    val user: User
): ViewModel() {

    private val _artistTopTracks = MutableLiveData<List<Track>>()
    val artistTopTracks : LiveData<List<Track>>
        get() = _artistTopTracks

    private val _artist = MutableLiveData<Artist>()
    val artist : LiveData<Artist>
        get() = _artist

    private val _artistAlbums = MutableLiveData<List<Album>>()
    val artistAlbums : LiveData<List<Album>>
        get() = _artistAlbums

    private val _isFollowingArtist = MutableLiveData<Boolean>()
    val isFollowingArtist : LiveData<Boolean>
        get() = _isFollowingArtist

    private val _isNavigateToMoreTracks = MutableLiveData<PlaylistInfo?>()
    val isNavigateToMoreTracks : LiveData<PlaylistInfo?>
        get() = _isNavigateToMoreTracks

    private val _isNavigateToMoreAlbums = MutableLiveData<PlaylistInfo?>()
    val isNavigateToMoreAlbums : LiveData<PlaylistInfo?>
        get() = _isNavigateToMoreAlbums

    private val _navigateToDetailPlaylist= MutableLiveData<PlaylistInfo>()
    val navigateToDetailPlaylist : LiveData<PlaylistInfo>
        get() = _navigateToDetailPlaylist

    private val job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    init {
        getArtistTopTracks()
        getArtistProfile()
        getArtistAlbums()
        checkIsFollowingArtist()
    }

    private fun getArtistTopTracks() {
        uiScope.launch {
            _artistTopTracks.value = try {
                val artistTopTracksDeferred = ToneApi.retrofitService
                    .getArtistTopTracksAsync(
                        "Bearer $token",
                        playlistInfo.id,
                        "VN"
                    )
                artistTopTracksDeferred.tracks!!
            } catch (e: Exception) {
                Log.i("error", e.message!!)
                listOf()
            }
        }
    }

    private fun getArtistProfile(){
        uiScope.launch {
           _artist.value =
               try {
                   ToneApi.retrofitService
                       .getArtist(
                           "Bearer $token",
                           playlistInfo.id)
               }catch (e: Exception){
                   Log.i("getArtist","Failure $e")
                   Artist()
               }
        }
    }

    private fun getArtistAlbums(){
        uiScope.launch {
            _artistAlbums.value =
                try{
                    ToneApi.retrofitService
                    .getArtistAlbums(
                        "Bearer $token",
                        playlistInfo.id
                    ).items
            }catch (e: Exception){
                Log.i("getArtistAlbums","Failure $e")
                listOf()
            }
        }
    }

    private fun checkIsFollowingArtist(){
        uiScope.launch {
            _isFollowingArtist.value =
                try {
                    ToneApi.retrofitService
                        .checkUserIsFollowingArtist(
                            "Bearer $token",
                            playlistInfo.id
                        )[0]
                }catch (e: Exception){
                    Log.i("isFollowingArtist","Failure $e")
                    false
                }
        }
    }

    fun changeStateFollowArtist(){
        if(_isFollowingArtist.value != true){
            followArtist()
        }else
            unfollowArtist()
        _isFollowingArtist.value = !_isFollowingArtist.value!!
    }

    private fun unfollowArtist(){
        uiScope.launch {
            try {
                ToneApi.retrofitService
                    .unfollowArtist(
                        "Bearer $token",
                        playlistInfo.id
                    )
            }catch (e: Exception){
                Log.i("unfollowArtist","Failure $e")
            }
        }
    }

    private fun followArtist(){
        uiScope.launch {
            try {
                ToneApi.retrofitService
                    .followArtist(
                        "Bearer $token",
                        playlistInfo.id)
            }catch (e: Exception){
                Log.i("followArtist","Failure $e")
            }
        }
    }

    fun navigateToMoreTracks(){
        _isNavigateToMoreTracks.value = playlistInfo
    }

    fun navigateToMoreTracksComplete(){
        _isNavigateToMoreTracks.value = null
    }

    fun navigateToMoreAlbums(){
        _isNavigateToMoreAlbums.value = playlistInfo
    }

    fun navigateToMoreAlbumsComplete(){
        _isNavigateToMoreAlbums.value = null
    }

    fun displayPlaylistDetails(dataItem: LibraryAdapter.DataItem){
        _navigateToDetailPlaylist.value = PlaylistInfo(
            dataItem.id.toString(),
            dataItem.name.toString(),
            dataItem.description.toString(),
            dataItem.image,
            dataItem.uri.toString(),
            dataItem.typeName.toString()
        )
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun displayPlaylistDetailsComplete() {
        _navigateToDetailPlaylist.value = null
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}