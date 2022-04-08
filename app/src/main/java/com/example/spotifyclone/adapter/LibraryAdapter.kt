package com.example.spotifyclone.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.*
import androidx.core.view.allViews
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifyclone.network.*
import com.example.tonezone.databinding.ItemArtistBinding
import com.example.tonezone.databinding.ItemPlaylistInListBinding
import com.example.tonezone.databinding.ItemTrackBinding
import kotlin.math.abs
import kotlin.math.min


class LibraryAdapter(private val clickListener: OnClickListener): ListAdapter<LibraryAdapter.DataItem, RecyclerView.ViewHolder>(
    DiffCallBack
) {

    private val ARTIST = 2
    private val PLAYLIST = 1
    private val TRACK = 3
    private val ALBUM = 4
    private var limit = -1

    companion object DiffCallBack: DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return newItem.id== oldItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return newItem == oldItem
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLimitItem(limit: Int){
        this.limit = limit
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (limit!=-1 && super.getItemCount()>limit){
            limit
        } else {
            super.getItemCount()
        }
    }

    override fun getItemViewType(position: Int): Int {
            return when (getItem(position)) {
                is DataItem.ArtistItem -> ARTIST
                is DataItem.TrackItem -> TRACK
                is DataItem.AlbumItem -> ALBUM
                else -> PLAYLIST

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1 -> PlaylistViewHolder.from(parent)
            2 -> ArtistViewHolder.from(parent)
            4 -> AlbumViewHolder.from(parent)
            else -> TrackViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is ArtistViewHolder -> {
                    val artistItem = getItem(position) as DataItem.ArtistItem
                    holder.bind(artistItem.artist, clickListener)
                }

                is PlaylistViewHolder -> {
                    val playlistItem = getItem(position) as DataItem.PlaylistItem
                    holder.bind(playlistItem.playlist, clickListener)
                }

                is TrackViewHolder -> {
                    val trackItem = getItem(position) as DataItem.TrackItem
                    holder.bind(trackItem.track, clickListener)
                }

                is AlbumViewHolder -> {
                    val albumItem = getItem(position) as DataItem.AlbumItem
                    holder.bind(albumItem.album, clickListener)
                }
            }
        }


    private var defaultData = listOf<DataItem>()
    fun submitYourLibrary(playlists: List<Playlist>?, artists: List<Artist>?, tracks: List<Track>?, albums: List<Album>?){
        val items =
                    (artists?.map { DataItem.ArtistItem(it) } ?: listOf()) +
                    (playlists?.map { DataItem.PlaylistItem(it) } ?: listOf()) +
                    (tracks?.map { DataItem.TrackItem(it) } ?: listOf())  +
                    (albums?.map { DataItem.AlbumItem(it) } ?: listOf())

            defaultData = items

    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterQuery(query: String) {
        val items = mutableListOf<DataItem>()
        if (query.isNotEmpty()) {
            for (item in defaultData) {
                if (item.name?.lowercase()?.contains(query.lowercase()) == true) {
                    items.add(item)
                }
            }
        }
        if(query.isNullOrEmpty()) {
            submitList(defaultData)
        }else{
            submitList(items)
        }
        notifyDataSetChanged()
    }

    fun filterType(type: String){
        val items = mutableListOf<DataItem>()

        if (type!="all") {
            for (item in defaultData) {
                if (item.typeName == type) {
                    items.add(item)
                }
            }
        } else{
            items.addAll(defaultData)
        }

        updateData(items)
    }

    fun sortByAlphabetical(){
        defaultData = defaultData.sortedBy { it.name }
        updateData(defaultData)
    }

    fun sortByCreator(){
        defaultData = defaultData.sortedBy { it.typeName }
        updateData(defaultData)
    }

    fun sortByMostRelate(keyWord: String){
        Log.i("keyWord",keyWord.toString())
        defaultData = defaultData.sortedBy {
            val similarityName =
            abs(it.name!!.lowercase().compareTo(keyWord))
            val similarityDescription = abs(it.description!!.lowercase().compareTo(keyWord))
            min(similarityName,similarityDescription)
        }
        updateData(defaultData)
    }

    fun sortByDefault(){
        updateData(defaultData)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<DataItem>){
        submitList(data)
        notifyDataSetChanged()
    }

    class ArtistViewHolder private constructor(
        private val binding: ItemArtistBinding
    ): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("ClickableViewAccessibility")
        fun bind(artist: Artist, clickListener: OnClickListener){
            binding.artist = artist
            binding.clickListener = clickListener
            binding.executePendingBindings()

            val gesture = CustomGesture().createGesture(clickListener,
                DataItem.ArtistItem(artist),itemView)
            itemView.allViews.all {
                it.setOnTouchListener { _, motionEvent ->
                    gesture.onTouchEvent(motionEvent)
                    true
                }
                true
            }
        }

        companion object{
            fun from(parent: ViewGroup): ArtistViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemArtistBinding.inflate(layoutInflater,parent,false)
                return ArtistViewHolder(binding)
            }
        }
    }

    class PlaylistViewHolder private constructor
        (private val binding: ItemPlaylistInListBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(playlist: Playlist, clickListener: OnClickListener){
            binding.playlist = playlist
            binding.clickListener = clickListener
            binding.executePendingBindings()

            val gesture = CustomGesture().createGesture(clickListener,
                DataItem.PlaylistItem(playlist),itemView)
            itemView.allViews.all {
                    it.setOnTouchListener { _, motionEvent ->
                        gesture.onTouchEvent(motionEvent)
                        true
                }
                true
            }
        }

        companion object{
            fun from(parent: ViewGroup): PlaylistViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPlaylistInListBinding.inflate(layoutInflater,parent,false)
                return PlaylistViewHolder(binding)
            }
        }
    }

    class TrackViewHolder private constructor
        (private val binding: ItemTrackBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(track: Track, clickListener: OnClickListener){
            binding.track = track

            binding.moreOptionWithTrack.setOnClickListener {
                clickListener.onClickMoreOption(track,binding.moreOptionWithTrack.id)
            }
            binding.executePendingBindings()

            val gesture = CustomGesture().createGesture(clickListener,
                DataItem.TrackItem(track),itemView)
            itemView.allViews.all {
                if(it.id!=binding.moreOptionWithTrack.id){
                    it.setOnTouchListener { _, motionEvent ->
                        gesture.onTouchEvent(motionEvent)
                        true
                    }
                }
                true
            }
        }

        companion object{
            fun from(parent: ViewGroup): TrackViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTrackBinding.inflate(layoutInflater,parent,false)
                return TrackViewHolder(binding)
            }
        }
    }

    class AlbumViewHolder private constructor
        (private val binding: ItemPlaylistInListBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(album: Album, clickListener: OnClickListener){
            val playlist = Playlist(false,
                album.id!!,
                "",
                "",
                album.images,
                album.name!!,
                Owner(),
                null,
                false,
                album.type!!,
                album.uri!!)
            binding.playlist = playlist
            binding.clickListener = clickListener
            binding.executePendingBindings()

            val gesture = CustomGesture().createGesture(clickListener,
                DataItem.PlaylistItem(playlist),itemView)
            itemView.allViews.all {
                it.setOnTouchListener { _, motionEvent ->
                    gesture.onTouchEvent(motionEvent)
                    true
                }
                true
            }
        }

        companion object{
            fun from(parent: ViewGroup): AlbumViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPlaylistInListBinding.inflate(layoutInflater,parent,false)
                return AlbumViewHolder(binding)
            }
        }
    }

    class CustomGesture(){
        fun createGesture(clickListener : OnClickListener, dataItem: DataItem, itemView: View) =
            GestureDetector(itemView.context,object : GestureDetector.SimpleOnGestureListener() {

                override fun onLongPress(e: MotionEvent?) {
                    clickListener.onLongPress(dataItem)
                }

                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    clickListener.onClick(dataItem)
                    return true
                }
            })
    }

    sealed class DataItem{
        data class PlaylistItem(val playlist: Playlist): DataItem(){
            override val id = playlist.id
            override val type = 1
            override val name = playlist.name
            override val typeName = playlist.type
            override val uri = playlist.uri
            override val description = playlist.owner.display_name
            override val image =
                if(playlist.images?.size!=0)
                playlist.images?.get(0)?.url
            else ""
        }

        data class ArtistItem( val artist: Artist): DataItem(){
            override val id = artist.id
            override val type = 2
            override val name = artist.name
            override val typeName = artist.type
            override val uri = artist.uri
            override val description = artist.type
            override val image =
                if(artist.images?.size!=0)
                    artist.images?.get(0)?.url
            else ""

        }

        data class TrackItem( val track: Track): DataItem(){
            override val id = track.id
            override val type = 3
            override val name = track.name
            override val typeName = "Track"
            override val uri = track.uri
            override val description =
                if(!track.artists.isNullOrEmpty()) {
                    var artistsDisplay = ""
                    track.artists.forEachIndexed { index, artist ->
                        artistsDisplay += artist.name + if (index != track.artists.size - 1) ", " else ""
                    }
                    artistsDisplay
                }
                else ""
            override val image = track.album?.uri

        }

        data class AlbumItem( val album: Album): DataItem(){
            override val id = album.id
            override val type = 4
            override val name = album.name
            override val typeName = album.type
            override val uri = album.uri
            override val description =
                if(!album.artists.isNullOrEmpty()){
                    val artistsDisplay = ""
                    album.artists.forEach {artistsDisplay.plus(it.name+" ")  }
                    artistsDisplay
                }
                else ""
            override val image = if(album.images?.size!=0)
                album.images?.get(0)?.url
            else ""

        }

        abstract val id: String?
        abstract val type: Int
        abstract val name: String?
        abstract val typeName: String?
        abstract val uri: String?
        abstract val description: String?
        abstract val image: String?
    }

    class OnClickListener(val clickListener: (dataItem: DataItem, idButton: Int?) -> Unit){
        fun onClick(playlist: Playlist) = clickListener(DataItem.PlaylistItem(playlist),null)
        fun onClick(artist: Artist) = clickListener(DataItem.ArtistItem(artist),null)
        fun onClick(track: Track) = clickListener(DataItem.TrackItem(track),null)
        fun onClick(dataItem: DataItem) = clickListener(dataItem,null)
        fun onClickMoreOption(track: Track, idButton: Int) = clickListener(DataItem.TrackItem(track),idButton)
        fun onLongPress(dataItem: DataItem) = clickListener(dataItem,Int.MIN_VALUE)
    }

}
