package com.example.spotifyclone

import android.text.format.DateUtils
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.spotifyclone.adapter.GroupPlaylistAdapter
import com.example.spotifyclone.adapter.LibraryAdapter
import com.example.spotifyclone.adapter.PlaylistAdapter
import com.example.spotifyclone.network.*
import com.example.spotifyclone.player.PlayerScreenViewModel
import com.example.spotifyclone.utils.Signal
import com.example.spotifyclone.utils.convertSignalToIcon
import com.example.spotifyclone.utils.convertSignalToText
import com.example.spotifyclone.yourlibrary.SortOption
import com.example.tonezone.R
import com.google.android.material.chip.Chip

@BindingAdapter("groupPlaylists")
fun bindGroupPlaylistsRecyclerview(recyclerView: RecyclerView, list: List<GroupPlaylist>?){
    if (list!=null){
        val adapter = recyclerView.adapter as GroupPlaylistAdapter
        adapter.submitList(list)
    }
}

@BindingAdapter("playlistInGridData")
fun bindPlaylistRecyclerview(recyclerView: RecyclerView, list: List<Playlist>?){
    if(list!=null){
        val adapter = recyclerView.adapter as PlaylistAdapter
        adapter.submitList(list)
    }
}

@BindingAdapter(value = ["playlistData","artistData","trackData","albumData","userSavedTracksData","sortOption","keyWord"],requireAll = false)
fun bindDataYourLibrary(recyclerView: RecyclerView,
                        playlistData: List<Playlist>?,
                        artistData: List<Artist>?,
                        trackData: List<Track>?,
                        albumData: List<Album>?,
                        userSavedTracksData: List<SavedTrack>?,
                        sortOption: SortOption?,
                        keyWord: String?
                        ){

    val adapter = recyclerView.adapter as LibraryAdapter

    val userSavedTracks =
        if(!userSavedTracksData.isNullOrEmpty())
            listOf(
                Playlist(false,"userSavedTrack","liked Songs",
            "", listOf(Image(null,url="https://picsum.photos/300/300",null)),"User's save songs",
                Owner(""),
            0,false,"playlist","")
            )
        else
            listOf()

    val playlists = if(playlistData!=null) userSavedTracks+playlistData else userSavedTracks
    val tracks = trackData ?: listOf()
    val artists = artistData ?: listOf()
    val albums = albumData ?: listOf()

    adapter.submitYourLibrary(playlists, artists, tracks, albums)
    when(sortOption){
        SortOption.Alphabetical -> adapter.sortByAlphabetical()
        SortOption.Creator -> adapter.sortByCreator()
        SortOption.MostRelate -> keyWord?.let { adapter.sortByMostRelate(it) }
        null -> adapter.sortByDefault()
    }

}

@BindingAdapter(value = ["imageUrl","listImageUrl"],requireAll = false)
fun bindImage(imageView: ImageView,imageUrl: String?,listImageUrl: List<Image>?){
//var numBlur = 0
//    if (blur == null)
//        numBlur=1
//    else
//        numBlur = blur

    if(listImageUrl?.size!=0) {
        Glide.with(imageView.context)
            .load(listImageUrl?.get(0)?.url)
            .apply(
               RequestOptions()
                   .placeholder(R.drawable.loading_animation)
                   .error(R.drawable.ic_connection_error)
            )
            .into(imageView)

    }
    if(imageUrl!=null)
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_connection_error)
                )
            .into(imageView)
}


@BindingAdapter("layoutVisibility")
fun bindLayoutVisibility(relativeLayout: RelativeLayout, track: Track?){

    relativeLayout.visibility = if(track!= Track() && track!=null) View.VISIBLE else View.GONE
}

@BindingAdapter("formatTime")
fun bindTime(textView: TextView, timeInt : Long?){
    timeInt?.let {
        textView.text = DateUtils.formatElapsedTime(timeInt/1000)
    }
}

@BindingAdapter("artists")
fun bindTextView(textView: TextView, list: List<Artist>){
    var artists = ""
    list.forEachIndexed { index, artist ->
        artists += artist.name+ if(index!=list.size-1) ", " else ""
    }
    textView.text = artists
}

@BindingAdapter("playerState")
fun bindStatePlayButton(button: ImageButton, state: PlayerScreenViewModel.PlayerState){
    when(state){
        PlayerScreenViewModel.PlayerState.PLAY -> button.setImageResource(R.drawable.ic_custom_pause)
        PlayerScreenViewModel.PlayerState.PAUSE -> button.setImageResource(R.drawable.ic_custom_play)
        else -> button.setImageResource(R.drawable.ic_custom_play)
    }
}

@BindingAdapter("isChoose")
fun bindColorShuffleButton(imageView: ImageView, isChoose: Boolean){
    if(isChoose)
        imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.colorSecondary))
    else
        imageView.setColorFilter(ContextCompat.getColor(imageView.context, R.color.gray))
}


@BindingAdapter("sizeList")
fun bindChip(chip: Chip, sizeList: Int?){
    if(sizeList!=0 && sizeList!=null)
        chip.visibility = View.VISIBLE
    else
        chip.visibility = View.GONE
}

@BindingAdapter("sizeSearchedItems")
fun bindContentSearchForItem(textView: TextView,size: Int?){
    when(size){
        0,null -> textView.visibility = View.VISIBLE
        else -> textView.visibility = View.GONE
    }
}

@BindingAdapter("signal")
fun setTextBottomSheetItem(button: Button, signal: Signal){
    button.text = convertSignalToText(signal)
}

@BindingAdapter("signalIcon")
fun setIconBottomSheetItem(button: Button,signal: Signal){
    val drawable = ContextCompat.getDrawable(button.context, convertSignalToIcon(signal))!!
    button.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable,null,null,null)
    button.compoundDrawablePadding = 36
}

@BindingAdapter("isVisibility")
fun setupButtonVisibility(imageButton: ImageButton, isOwned: Boolean){
    if(isOwned){
        imageButton.visibility = View.GONE
    }
    else imageButton.visibility = View.VISIBLE
}

@BindingAdapter("isFollowing")
fun bindTextButton(button: Button,isFollowing: Boolean){
    if(isFollowing){
        button.text = "following"
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.colorSecondary))
    }else {
        button.text = "follow"
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.colorPrimary))

    }
}

@BindingAdapter("profileVisibility")
fun bindPlaylistProfile(linearLayout: LinearLayout,playlistInfo: PlaylistInfo){
    if(playlistInfo.type!="artist")
        linearLayout.visibility = View.VISIBLE
    else
        linearLayout.visibility = View.GONE
}

@BindingAdapter("imageProfileVisibility")
fun bindImageProfile(imageView: ImageView,playlistInfo: PlaylistInfo){
    if(playlistInfo.type!="artist")
        imageView.visibility = View.VISIBLE
    else
        imageView.visibility = View.GONE
}