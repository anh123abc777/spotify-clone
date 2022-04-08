package com.example.spotifyclone.utils

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ModalBottomSheetViewModel: ViewModel() {

    private val _signal= MutableLiveData<Signal>()
    val signal: LiveData<Signal>
        get() = _signal

    fun sendSignal(signal: Signal){
        _signal.value = signal
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun sendSignalComplete(){
        _signal.value = null
    }
}