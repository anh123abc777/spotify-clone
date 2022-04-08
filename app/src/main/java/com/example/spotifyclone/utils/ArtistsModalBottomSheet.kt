package com.example.spotifyclone.utils

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tonezone.R
import com.example.spotifyclone.adapter.LibraryAdapter
import com.example.tonezone.databinding.ModalBottomSheetContentBinding
import com.example.spotifyclone.network.Artist
import com.example.spotifyclone.network.PlaylistInfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ArtistsModalBottomSheet (private val list: List<Artist>) : BottomSheetDialogFragment() {

    private lateinit var binding: ModalBottomSheetContentBinding

    private val viewModel: ModalBottomSheetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ModalBottomSheetContentBinding.inflate(inflater)

        setupBottomSheetItems()

        return binding.root

    }

    private fun setupBottomSheetItems(){

        val adapter = LibraryAdapter(LibraryAdapter.OnClickListener{ item, idButton ->
            val playlistInfo = PlaylistInfo(
                item.id.toString(),
                item.name.toString(),
                item.description.toString(),
                item.image,
                item.uri.toString(),
                item.description.toString()
            )
            Log.i("artists",playlistInfo.toString())
            val bundle = bundleOf("playlistInfo" to playlistInfo)
            findNavController().navigate(R.id.playlistDetailsFragment,bundle)
            this.dismiss()
        })

        adapter.submitList(list.map { LibraryAdapter.DataItem.ArtistItem(it) })
        Log.i("setUpShowingArtists",list.toString())
        binding.listOption.adapter = adapter
    }

    companion object{
        const val TAG = "ArtistsModalBottomSheet"
    }
}