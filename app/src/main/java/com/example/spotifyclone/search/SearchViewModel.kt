package com.example.spotifyclone.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotifyclone.network.Category
import com.example.spotifyclone.network.ToneApi
import kotlinx.coroutines.*

class SearchViewModel(private val token: String) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _categories = MutableLiveData<List<Category>>()
    val categories : LiveData<List<Category>>
        get() = _categories

    init {
        getGenres()
    }

    fun getGenres(){
        uiScope.launch {
            _categories.value = try {
                ToneApi.retrofitService
                    .getCategoriesAsync("Bearer $token").categories.items
            } catch (e: Exception) {
                Log.i("error", e.message.toString())
                listOf()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}