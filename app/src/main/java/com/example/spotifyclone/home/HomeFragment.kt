package com.example.spotifyclone.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.spotifyclone.MainViewModel
import com.example.spotifyclone.adapter.GroupPlaylistAdapter
import com.example.spotifyclone.adapter.PlaylistAdapter
import com.example.tonezone.databinding.FragmentHomeBinding
import com.example.spotifyclone.network.PlaylistInfo



class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(mainViewModel.token,mainViewModel.user.value!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater)

            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        createAdapterGroupPlaylist()
        observeNavigateToPlaylistDetails()
        binding.lifecycleOwner = this

    }

    private fun createAdapterGroupPlaylist(){
        val adapter = GroupPlaylistAdapter(PlaylistAdapter.OnClickListener {
           viewModel.displayPlaylistDetails(
               PlaylistInfo(
                    it.id,
                    it.name,
                    it.description,
                    it.images?.get(0)?.url,
                    it.uri,
                    it.type
                )
           )
        })
        binding.groupPlaylist.adapter = adapter
    }

    private fun observeNavigateToPlaylistDetails(){
        viewModel.navigateToPlaylistDetails.observe(viewLifecycleOwner){
            if(it!=null) {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToPlaylistDetailsFragment(it)
                )

                viewModel.displayPlaylistDetailsComplete()
            }
        }
    }

}