package com.example.spotifyclone.search.searchfoitem


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.spotifyclone.MainViewModel
import com.example.tonezone.R
import com.example.spotifyclone.adapter.LibraryAdapter
import com.example.spotifyclone.network.*
import com.example.spotifyclone.player.PlayerScreenViewModel
import com.example.tonezone.databinding.FragmentSearchForItemBinding
import com.example.spotifyclone.yourlibrary.TypeItemLibrary

class SearchForItemFragment : Fragment() {

    private lateinit var binding: FragmentSearchForItemBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private val viewModel: SearchForItemViewModel by viewModels {
        SearchForItemViewModelFactory(mainViewModel.token)
    }

    private val playerViewModel: PlayerScreenViewModel by activityViewModels()

    private lateinit var adapter: LibraryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchForItemBinding.inflate(inflater,container,false)

        binding.viewModel= viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupSearchbar()
        observeSearchedItems()
        setupFilterType()

        return binding.root
    }
//
//    @SuppressLint("Recycle")
//    private fun temp(){
//        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,4f)
//        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,4f)
//
//        val animator
//    }

    private fun setupSearchbar(){
        binding.searchBar.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                viewModel.search(p0)
            }
        })

        binding.searchBar.setOnFocusChangeListener { _, isFocusing ->
            if (isFocusing){
                binding.backSearchButton.setImageResource(R.drawable.ic_clear_text)
                binding.backSearchButton.setOnClickListener {
                    binding.searchBar.text?.clear()
                    binding.searchBar.clearFocus()
                    (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                            InputMethodManager).hideSoftInputFromWindow(binding.root.windowToken,0)
                }
            }
            else{
                binding.backSearchButton.setImageResource(R.drawable.ic_arrow_back)
                binding.backSearchButton.setOnClickListener {
                    requireActivity().onBackPressed()
                }
            }
        }


    }

    private fun bindChipGroup(){
        binding.chipGroup.filterTypeChipGroup.isSingleSelection = true
        binding.chipGroup.playlistData = viewModel.searchedItems.value?.playlists ?: Playlists()
        binding.chipGroup.artistData = viewModel.searchedItems.value?.artists ?: Artists()
        binding.chipGroup.trackData = viewModel.searchedItems.value?.tracks ?: Tracks()
        binding.chipGroup.albumData = viewModel.searchedItems.value?.albums ?: Albums(listOf())

        binding.chipGroup.filterTypeChipGroup.setOnCheckedChangeListener { _, checkedId ->

            when(checkedId){
                R.id.all_type -> viewModel.filterType(TypeItemLibrary.All)
                R.id.playlist_type -> viewModel.filterType(TypeItemLibrary.Playlist)
                R.id.artist_type -> viewModel.filterType(TypeItemLibrary.Artist)
                R.id.track_type -> viewModel.filterType(TypeItemLibrary.Track)
                R.id.albums_type -> viewModel.filterType(TypeItemLibrary.Album)
                else -> viewModel.filterType(TypeItemLibrary.All)
            }
        }
    }

    private fun observeSearchedItems(){
        adapter = LibraryAdapter(LibraryAdapter.OnClickListener { item, _ ->
            when(item.typeName!!.lowercase()){
                "artist" -> {
                    findNavController().navigate(
                        SearchForItemFragmentDirections
                            .actionSearchForItemFragmentToArtistDetailsFragment(
                                convertDataItemToObject(item)
                            ))
                }

                "album","playlist" -> {
                    findNavController().navigate(
                        SearchForItemFragmentDirections
                            .actionSearchForItemFragmentToPlaylistDetailsFragment(
                                convertDataItemToObject(item)
                            )
                    )
                }

                "track" -> {
                    Toast.makeText(context,item.name.toString(),Toast.LENGTH_SHORT).show()
                    playerViewModel.onPlay(item.uri,null)
                }
            }
        })
        binding.searchedItems.adapter = adapter
        viewModel.searchedItems.observe(viewLifecycleOwner){
            if (it!=null)
                bindChipGroup()
        }
    }

    private fun convertDataItemToObject(item: LibraryAdapter.DataItem)=
        PlaylistInfo(
            item.id!!,
            item.name!!,
            item.description!!,
            item.image,
            item.uri!!,
            item.typeName!!
        )


    private fun setupFilterType(){
        viewModel.type.observe(viewLifecycleOwner){
            when(it){
                TypeItemLibrary.All -> adapter.filterType("all")
                TypeItemLibrary.Playlist -> adapter.filterType("playlist")
                TypeItemLibrary.Artist -> adapter.filterType("artist")
                TypeItemLibrary.Album -> adapter.filterType("album")
                else -> adapter.filterType("Track")
            }
        }
    }
}