package com.example.spotifyclone.yourlibrary

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.spotifyclone.MainViewModel
import com.example.spotifyclone.MainViewModelFactory
import com.example.tonezone.R
import com.example.spotifyclone.adapter.LibraryAdapter
import com.example.tonezone.databinding.FragmentYourLibraryBinding
import com.example.spotifyclone.network.Albums
import com.example.spotifyclone.utils.ModalBottomSheet
import com.example.spotifyclone.utils.ModalBottomSheetViewModel
import com.example.spotifyclone.utils.ObjectRequest
import com.example.spotifyclone.utils.convertSignalToText


@Suppress("DEPRECATION")
class YourLibraryFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModelFactory(requireActivity())
    }
    private val viewModel: YourLibraryViewModel by viewModels {
        YourLibraryViewModelFactory(mainViewModel.token,mainViewModel.user.value!!)
    }

    private val modalBottomSheetViewModel : ModalBottomSheetViewModel by activityViewModels()

    private val ARTIST = 2
    private val PLAYLIST = 1

    private lateinit var adapter : LibraryAdapter

    private lateinit var binding: FragmentYourLibraryBinding

    private lateinit var modalBottomSheet: ModalBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentYourLibraryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getDataUserPlaylists()

        setupYourLibraryAdapter()
        observeNavigateToPlaylistDetails()
        setupMenuAppbarOnClick()
        bindChipGroup()
        setupSearchBar()
        setupSortOption()
        setupFilterType()
        handleRequestToCreatePlaylist()
        setupBottomSheet()
        handleSignalFromBottomSheet()

        return binding.root
    }

        private fun setupFilterType() {
        viewModel.type.observe(viewLifecycleOwner) {
            when (it) {
                TypeItemLibrary.All -> adapter.filterType("all")
                TypeItemLibrary.Playlist -> adapter.filterType("playlist")
                TypeItemLibrary.Artist -> adapter.filterType("artist")
                TypeItemLibrary.Album -> adapter.filterType("album")
                else -> throw IllegalArgumentException("unknown value")
            }
        }
    }

    private fun setupSortOption() {

        binding.displayOption.viewModel = viewModel

        viewModel.sortOption.observe(viewLifecycleOwner){
            when(it){
                SortOption.Alphabetical -> adapter.sortByAlphabetical()
                SortOption.Creator -> adapter.sortByCreator()
                else -> throw IllegalArgumentException("Unknown value")
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun bindChipGroup() {
        binding.chipGroup.filterTypeChipGroup.isSingleSelection = true

        viewModel.followedArtists.observe(viewLifecycleOwner) {
            binding.chipGroup.artistData = it
        }

        viewModel.userPlaylists.observe(viewLifecycleOwner) {
            binding.chipGroup.playlistData = it
        }

        viewModel.saveAlbums.observe(viewLifecycleOwner){
            binding.chipGroup.albumData = Albums(it)
        }

        binding.chipGroup.filterTypeChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.all_type -> viewModel.filterType(TypeItemLibrary.All)
                R.id.playlist_type -> viewModel.filterType(TypeItemLibrary.Playlist)
                R.id.artist_type -> viewModel.filterType(TypeItemLibrary.Artist)
                R.id.albums_type -> viewModel.filterType(TypeItemLibrary.Album)
                else -> viewModel.filterType(TypeItemLibrary.All)
            }
        }
    }

    private fun setupSearchBar() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(query!=null && query.isNotBlank()) {
                    (binding.yourLibraryList.adapter as LibraryAdapter).filterQuery(query.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setupMenuAppbarOnClick() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> {
                    binding.toolbar.menu.setGroupVisible(R.id.menu_appbar_yourlibrary, false)
                    binding.searchBar.visibility = View.VISIBLE
                    binding.searchBar.requestFocus()
                    binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
                    binding.displayOption.arrangementFrame.visibility = View.GONE
                    binding.toolbar.setNavigationOnClickListener {
                        binding.searchBar.visibility = View.GONE
                        binding.toolbar.menu.setGroupVisible(R.id.menu_appbar_yourlibrary, true)
                        binding.toolbar.navigationIcon = null
                        binding.searchBar.clearFocus()
                        binding.searchBar.text.clear()
                        binding.displayOption.arrangementFrame.visibility = View.VISIBLE
                    }
                }

                R.id.add_playlist -> {
                    viewModel.requestToCreatePlaylist()
                }
            }
            true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleRequestToCreatePlaylist(){
        viewModel.isCreatingPlaylist.observe(viewLifecycleOwner){
            if (it){

                val alert = AlertDialog.Builder(context,R.style.AlertDialogTheme)
                val input = EditText(context)
                input.inputType = InputType.TYPE_CLASS_TEXT
                input.setBackgroundColor(Color.WHITE)
                input.gravity = Gravity.CENTER
                input.setText("Playlist")
                alert.setView(input)

                alert.setPositiveButton("Ok"
                ) { dialog, _ ->
                    viewModel.createPlaylist(input.text.toString())
                    dialog.dismiss()
                }

                alert.setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.cancel()
                }

                alert.show()

//                input.setOnFocusChangeListener { _, b ->
//                    if(b)
//                        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
//                                InputMethodManager).toggleSoftInputFromWindow(
//                            binding.root.windowToken,
//                            InputMethodManager.SHOW_FORCED,
//                            0
//                        )
//                    else{
//                        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
//                                InputMethodManager).hideSoftInputFromWindow(
//                            binding.root.windowToken,
//                            0)
//                    }
//                }

                input.requestFocus()
                viewModel.requestToCreatePlaylistComplete()
            }
        }
    }

    private fun setupYourLibraryAdapter() {

        adapter = LibraryAdapter(LibraryAdapter.OnClickListener { item, idButton ->
            when(idButton){
                null -> viewModel.displayPlaylistDetails(item)
                Int.MIN_VALUE -> {
                    viewModel.showBottomSheet(getObjectRequestFromTypeItem(item), item.id!!)
                }
                else -> Log.i("libraryAdapter","Nothing")
                }
        })

        binding.yourLibraryList.adapter = adapter
    }

    private fun getObjectRequestFromTypeItem(dataItem: LibraryAdapter.DataItem): ObjectRequest
        = when(dataItem.type){
            ARTIST -> ObjectRequest.ARTIST_FROM_LIBRARY
            PLAYLIST -> {
                val playlist = (dataItem as LibraryAdapter.DataItem.PlaylistItem).playlist
                if(playlist.owner.id==viewModel.user.id)
                    ObjectRequest.OWNER_PLAYLIST_FROM_LIBRARY
                else
                    ObjectRequest.PLAYLIST_FROM_LIBRARY
            }
            else -> ObjectRequest.PLAYLIST_FROM_LIBRARY
        }


    private fun handleSignalFromBottomSheet(){

        modalBottomSheetViewModel.signal.observe(viewLifecycleOwner){
            when(it){
                null -> Log.i("receivedSignal","unknown value")
                else -> {
                    viewModel.receiveSignal(it)
                    Log.i("receivedSignal", convertSignalToText(it))
                }
            }
        }

        viewModel.receivedSignal.observe(viewLifecycleOwner){
            if (it!=null) {
                modalBottomSheet.dismiss()
                viewModel.handleSignal()
                viewModel.handleSignalComplete()
            }
        }
    }

    private fun setupBottomSheet(){
        viewModel.objectShowBottomSheet.observe(viewLifecycleOwner){
            when(it){
                null -> Log.i("setupBottomSheet","objectRequest null")
                else -> {
                    modalBottomSheet = ModalBottomSheet(it.first,null)
                    modalBottomSheet.show(
                        requireActivity().supportFragmentManager,
                        ModalBottomSheet.TAG
                    )
                }

            }
        }
    }

    private fun observeNavigateToPlaylistDetails() {
        viewModel.navigateToDetailPlaylist.observe(viewLifecycleOwner) {
            if (it!=null){
                when(it.type) {
                    "playlist","album" -> this.findNavController()
                        .navigate(
                            YourLibraryFragmentDirections
                                .actionYourLibraryFragmentToPlaylistDetailsFragment(it)
                        )

                    "artist" -> this.findNavController()
                        .navigate(YourLibraryFragmentDirections
                            .actionYourLibraryFragmentToArtistDetailsFragment(it))

                }
                viewModel.displayPlaylistDetailsComplete()
            }
        }
    }

}