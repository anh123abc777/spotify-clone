package com.example.spotifyclone

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.REDIRECT_URI
import com.example.spotifyclone.network.ToneApi
import com.example.spotifyclone.network.User
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE
import kotlinx.coroutines.launch

class MainViewModel(private val activity: Activity): ViewModel() {

    var token = ""

    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    private val builder =
        AuthorizationRequest.Builder(
            "0546209c8b9b4b66a8d49037c566caa6",
            AuthorizationResponse.Type.TOKEN,
            REDIRECT_URI
        )

    fun initAuthorization(){
        builder.setScopes(arrayOf("streaming",
            "playlist-read-private",
            "playlist-read-collaborative",
            "user-follow-read",
            "user-library-read",
            "playlist-modify-private",
            "playlist-modify-public",
            "user-library-modify",
            "user-follow-modify",
        ))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(activity, REQUEST_CODE, request)

    }

    fun getCurrentUserProfileData(){
        viewModelScope.launch {
            _user.value = try {
                ToneApi.retrofitService.getCurrentUserProfile("Bearer $token")
            }catch (e: Exception){
                User()
            }
        }
    }

}