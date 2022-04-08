package com.example.spotifyclone.search.searchfoitem

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spotifyclone.network.SearchedItem
import com.example.spotifyclone.network.ToneApi
import com.example.spotifyclone.yourlibrary.TypeItemLibrary
import kotlinx.coroutines.*

class SearchForItemViewModel(val token: String): ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _type = MutableLiveData<TypeItemLibrary>()
    val type : LiveData<TypeItemLibrary>
        get() = _type

    private var _searchedItems = MutableLiveData<SearchedItem>()
    val searchedItems : LiveData<SearchedItem>
        get() = _searchedItems

    private val _searchKey = MutableLiveData<String>()
    val searchKey : LiveData<String>
        get() = _searchKey

    fun search(query: Editable?) {
        if (query != null)
            if(query.isNotBlank() && query.isNotEmpty()){
                _searchKey.value = query.toString()
                uiScope.launch {
                    _searchedItems.value = try {

                        ToneApi.retrofitService
                            .searchForItemAsync("Bearer $token", _searchKey.value!!)

                    } catch (e: Exception) {

                        SearchedItem()

                    }
                }
            }
            else
                _searchedItems.value = SearchedItem()

        else
            _searchedItems.value = SearchedItem()
    }

    fun filterType(type: TypeItemLibrary){
        _type.value = type
    }

}