package com.example.spotifyclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tonezone.databinding.ItemPlaylistInGridBinding
import com.example.spotifyclone.network.Playlist

class PlaylistAdapter(private val clickListener: OnClickListener) : ListAdapter<Playlist, PlaylistAdapter.ViewHolder>(
    DiffCallBack
){

    companion object DiffCallBack: DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder private constructor
        (private val binding: ItemPlaylistInGridBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist, clickListener: OnClickListener){
            binding.playlist = playlist
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPlaylistInGridBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newItem = getItem(position)
        holder.bind(newItem,clickListener)
    }

    class OnClickListener(val clickListener : (playlist: Playlist) -> Unit) {

        fun onClick(playlist: Playlist) = clickListener(playlist)
    }
}
