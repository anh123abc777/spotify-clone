package com.example.spotifyclone.yourlibrary

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.adapter.LibraryAdapter
import com.example.spotifyclone.network.*
import com.example.spotifyclone.utils.ObjectRequest
import com.example.spotifyclone.utils.Signal
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class YourLibraryViewModel(val token: String, val user: User) : ViewModel() {

    private val job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    private val _userPlaylists = MutableLiveData<Playlists>()
    val userPlaylists : LiveData<Playlists>
        get() = _userPlaylists

     private val _followedArtists = MutableLiveData<Artists>()
    val followedArtists : LiveData<Artists>
        get() = _followedArtists

    private val _savedTracks = MutableLiveData<SavedTracks>()
    val savedTracks : LiveData<SavedTracks>
        get() = _savedTracks

    private val _savedAlbums = MutableLiveData<List<Album>>()
    val saveAlbums : LiveData<List<Album>>
        get() = _savedAlbums

    private val _sortOption = MutableLiveData<SortOption>()
    val sortOption : LiveData<SortOption>
        get() = _sortOption

    private val _type = MutableLiveData<TypeItemLibrary>()
    val type : LiveData<TypeItemLibrary>
        get() = _type

    private val _navigateToDetailPlaylist= MutableLiveData<PlaylistInfo>()
    val navigateToDetailPlaylist : LiveData<PlaylistInfo>
        get() = _navigateToDetailPlaylist

    private val _objectShowBottomSheet = MutableLiveData<Pair<ObjectRequest,String>>()
    val objectShowBottomSheet : LiveData<Pair<ObjectRequest,String>>
        get() = _objectShowBottomSheet

    private var _receivedSignal = MutableLiveData<Signal>()
    val receivedSignal : LiveData<Signal>
        get() = _receivedSignal

    init {
        getDataUserSavedTracks()
        getDataFollowedArtists()
        getDataUserPlaylists()
        getDataSavedAlbums()
        _sortOption.value = SortOption.Alphabetical
        _type.value = TypeItemLibrary.All

    }

     fun getDataUserPlaylists(){
        viewModelScope.launch {
            try {
                _userPlaylists.value = ToneApi.retrofitService
                    .getCurrentUserPlaylistsAsync("Bearer $token")

            } catch (e: Exception) {
                Log.i("error", e.message!!)
            }
        }
    }

    private fun getDataFollowedArtists(){
        viewModelScope.launch {
            try {
                _followedArtists.value = ToneApi.retrofitService
                    .getFollowedArtistsAsync("Bearer $token", "artist").artists!!

            } catch (e: Exception) {
                Log.i("errorFollowedArtists", e.message!!)
                _followedArtists.value = Artists()
            }
        }
    }

    private fun getDataUserSavedTracks(){
        viewModelScope.launch {
            _savedTracks.value = try {
                ToneApi.retrofitService.getUserSavedTracks("Bearer $token")
            }catch (e: Exception){
                Log.i("errorUserSavedTracks",e.message.toString())
                SavedTracks()
            }
        }
    }

    private fun getDataSavedAlbums(){
        viewModelScope.launch {
            _savedAlbums.value = try {
                ToneApi.retrofitService.getSavedAlbums("Bearer $token").items.map { it.album }

            }catch (e: Exception){
                Log.i("getDataSavedAlbums","Failure $e")
                listOf()
            }
        }
    }

    fun changeSortOption(){
        _sortOption.value =
            if(_sortOption.value== SortOption.Alphabetical)
                SortOption.Creator
            else SortOption.Alphabetical
    }

    fun filterType(type: TypeItemLibrary){
        _type.value = type
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

    fun displayPlaylistDetails(playlist: Playlist){
        _navigateToDetailPlaylist.value = PlaylistInfo(
            playlist.id,
            playlist.name,
            playlist.description,
            if (playlist.images?.isNotEmpty() == true)
                playlist.images[0].url
            else "",
            playlist.uri,
            playlist.type
        )
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun displayPlaylistDetailsComplete() {
        _navigateToDetailPlaylist.value = null
    }

    private val _isCreatingPlaylist = MutableLiveData<Boolean>()
    val isCreatingPlaylist : LiveData<Boolean>
        get() = _isCreatingPlaylist

    fun requestToCreatePlaylist(){
        _isCreatingPlaylist.value = true
    }

    fun requestToCreatePlaylistComplete(){
        _isCreatingPlaylist.value = false
    }

    fun createPlaylist(playlistName: String){
        uiScope.launch {
            try {

                ToneApi.retrofitService2
                    .createPlaylist("Bearer $token",
                        user.id,
                        "{\"name\":\"$playlistName\"," +
                                "\"description\":\"New playlist description\"," +
                                "\"public\":false}").enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val gson = Gson()
                            val playlist = gson.fromJson(response.body(), Playlist::class.java)
                            getDataUserPlaylists()
                            displayPlaylistDetails(playlist)
                            Log.i("createPlaylist",playlist.toString())
                        }
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.i("createPlaylist","Failure + ${t.message}")
                        }
                    })
            } catch (e: Exception){

                Log.i("createPlaylist","Failure + $e")
            }
        }
    }

    fun showBottomSheet(objectRequest: ObjectRequest, itemId : String){
        _objectShowBottomSheet.value = Pair(objectRequest,itemId)
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun showBottomSheetComplete(){
        _objectShowBottomSheet.value = null
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun handleSignalComplete(){
        _receivedSignal.value = null
    }

    fun handleSignal(){
        when(_receivedSignal.value){
            null -> Log.i("receivedSignal","don't have happen")

            Signal.LIKED_PLAYLIST -> unlikePlaylist()

            Signal.DELETE_PLAYLIST -> deletePlaylist()

            Signal.STOP_FOLLOWING -> unfollowArtist()

            else -> Log.i("receivedSignal","what is this???????")
        }
    }

    private fun unfollowArtist(){
        uiScope.launch {
            try {
                ToneApi.retrofitService
                    .unfollowArtists(
                        "Bearer $token",
                    _objectShowBottomSheet.value!!.second)
                Log.i("unfollowArtist","success")


            }catch (e: Exception){
                Log.i("unfollowArtist","Failure ${e.message.toString()}")
            }
        }
        getDataFollowedArtists()

    }

    private fun unlikePlaylist(){
        uiScope.launch {
            try {
                    ToneApi.retrofitService
                        .unfollowPlaylist(
                            "Bearer $token",
                            _objectShowBottomSheet.value!!.second)
                getDataUserPlaylists()
            }catch (e: Exception){
                Log.i("errorLikePlaylist",e.message.toString())
            }
        }
    }

    private fun deletePlaylist(){
        uiScope.launch {
            try {
                ToneApi.retrofitService.unfollowPlaylist(
                    "Bearer $token",
                    _objectShowBottomSheet.value!!.second)
                getDataUserPlaylists()
            }catch (e: Exception){
                Log.i("deletePlaylist",e.message.toString())
            }
        }
    }

    fun receiveSignal(signal: Signal){
        _receivedSignal.value = signal
    }

    fun addItemToPlaylist(playlistID: String, trackUris: String){
        Log.i("addItemToPlaylist","playlistID: $playlistID \n trackUris: $trackUris")
        uiScope.launch {
            try {
                ToneApi.retrofitService2.addItemsToPlaylist(
                    "Bearer $token",
                    playlistID,
                    trackUris
                    ).enqueue(object: Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.i("addItemToPlaylist","success ${response.body()}")
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.i("addItemToPlaylist","Failure s $t")

                    }
                })
            }catch (e: Exception){
                Log.i("addItemToPlaylist","Failure $e")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}