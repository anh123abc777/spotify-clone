package com.example.spotifyclone.search.searchfoitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class SearchForItemViewModelFactory (val token: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchForItemViewModel::class.java))
            return SearchForItemViewModel(token) as T
        throw IllegalArgumentException("Unknown VM class")
    }
}