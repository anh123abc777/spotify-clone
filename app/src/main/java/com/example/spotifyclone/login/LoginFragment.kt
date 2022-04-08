package com.example.spotifyclone.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.spotifyclone.MainViewModel
import com.example.spotifyclone.MainViewModelFactory
import com.example.tonezone.R

class LoginFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainViewModel.initAuthorization()

        return inflater.inflate(R.layout.fragment_login, container, false)
    }


}