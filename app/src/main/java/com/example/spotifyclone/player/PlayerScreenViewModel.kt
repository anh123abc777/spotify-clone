package com.example.spotifyclone.player

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotifyclone.database.TokenRepository
import com.example.spotifyclone.database.TonezoneDB
import com.example.spotifyclone.network.ToneApi
import com.example.spotifyclone.network.Track
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException


class PlayerScreenViewModel(val application: Application) : ViewModel() {

    private val CLIENT_ID = "0546209c8b9b4b66a8d49037c566caa6"
    private val REDIRECT_URI = "com.tonezone://callback"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private var connectionParams = ConnectionParams.Builder(CLIENT_ID)
        .setRedirectUri(REDIRECT_URI)
        .showAuthView(true)
        .build()!!

    private val tokenRepository = TokenRepository(TonezoneDB.getInstance(application).tokenDao)
    val token = runBlocking(Dispatchers.IO) { tokenRepository.token}
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope( Dispatchers.Main)
    private val uiSeekBarScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _currentTrack = MutableLiveData<Track>()
    val currentTrack : LiveData<Track>
        get() = _currentTrack

    private var _playerState = MutableLiveData<PlayerState>()
    val playerState : LiveData<PlayerState>
        get() = _playerState

    private var _progress = MutableLiveData<Long>()
    val progress : LiveData<Long>
        get() = _progress

    private var _uriTrackResponse = MutableLiveData<String>()
    val uriTrackResponse : LiveData<String>
        get() = _uriTrackResponse

    private var _currentPlaylist = MutableLiveData<List<Track>>()
    val currentPlaylist : LiveData<List<Track>>
        get() = _currentPlaylist

    private var _isShuffling = MutableLiveData<Boolean>()
    val isShuffling : LiveData<Boolean>
        get() = _isShuffling
    init {
        onStart()
        _currentTrack.value = Track()
        _playerState.value = PlayerState.NONE
        _progress.value = 0L
        _isShuffling.value = false
    }

    private fun onStart(){
        SpotifyAppRemote.connect(application, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")
                }
                override fun onFailure(throwable: Throwable) {
                    Log.e("MyActivity", throwable.message, throwable)
                }
            })
    }

    fun onPlay(uriPlaylist: String?, pos: Int?,listTrack : List<Track>?=listOf()) {

        _currentPlaylist.value = listTrack!!
        if(pos==null){
            mSpotifyAppRemote!!.playerApi.play(uriPlaylist)
        }else
            mSpotifyAppRemote!!.playerApi.skipToIndex(uriPlaylist,pos)
        mSpotifyAppRemote!!.playerApi
            .subscribeToPlayerState()
            .setEventCallback { playerState ->
                if(playerState.track.uri!=null)
                _uriTrackResponse.value = playerState.track.uri
            }
        _playerState.value = PlayerState.PLAY

    }

     var jobs = uiSeekBarScope.coroutineContext.job

    fun initSeekBar(){
         jobs = uiSeekBarScope.launch {

             var currentPosition = 0L
             while (this.isActive) {
                 if (_playerState.value == PlayerState.PLAY) {
                     if (currentPosition != currentTrack.value!!.duration_ms) {
                         mSpotifyAppRemote!!.playerApi
                             .subscribeToPlayerState()
                             .setEventCallback { state ->
                                 currentPosition = state.playbackPosition
                                 _progress.value = currentPosition
                                 _isShuffling.value = state.playbackOptions.isShuffling
                             }
                         Log.i("initSeekBar", "$this ${this.isActive}")
                         delay(500L)
                     }
                 } else {
                     this.cancel()
                     Log.i("initSeekBar", "$this ${this.isActive}")
                 }
             }
         }
        }

    fun getImageTrack() =  uiScope.launch {
        _currentTrack.value = try {
            val id = _uriTrackResponse.value
                ?.substring(_uriTrackResponse.value!!.lastIndexOf(":")+1, _uriTrackResponse.value!!.length)

            ToneApi.retrofitService
                .getTrackAsync("Bearer ${token.value!!.value}", id!!)

        }catch (e: Exception){
            Track()
        }

    }


    fun onShuffle(){
        mSpotifyAppRemote!!.playerApi.setShuffle(!_isShuffling.value!!)
    }

    fun onRepeat(){
        mSpotifyAppRemote!!.playerApi.setRepeat(1)
    }

    fun onChangeState(){
        when(_playerState.value){
            PlayerState.PAUSE -> onResume()
            PlayerState.PLAY -> onPause()
            else -> throw IllegalArgumentException("nothing")
        }
    }

    private fun onPause() {
        mSpotifyAppRemote!!.playerApi.pause()
        _playerState.value = PlayerState.PAUSE
        jobs.cancel()
    }

    private fun onResume(){
        mSpotifyAppRemote!!.playerApi.resume()
        _playerState.value = PlayerState.PLAY
        initSeekBar()
    }

    fun onNext() {
        jobs.cancel()
        mSpotifyAppRemote!!.playerApi.skipNext()
        _playerState.value = PlayerState.PLAY

    }

    fun onPrevious(){
        jobs.cancel()
        mSpotifyAppRemote!!.playerApi.skipPrevious()
        _playerState.value = PlayerState.PLAY
    }

    fun seekTo(posMs: Long){
        mSpotifyAppRemote!!.playerApi.seekTo(posMs)
    }

    fun disconnect(){
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
        _playerState.value = PlayerState.NONE
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    enum class PlayerState{PLAY, PAUSE, NONE}
}