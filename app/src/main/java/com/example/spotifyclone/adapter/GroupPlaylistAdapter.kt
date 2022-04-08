package com.example.spotifyclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tonezone.databinding.ItemGroupPlaylistBinding
import com.example.spotifyclone.network.GroupPlaylist

class GroupPlaylistAdapter(private val clickListener: PlaylistAdapter.OnClickListener) : ListAdapter<GroupPlaylist, GroupPlaylistAdapter.ViewHolder>(
    DiffCallBack
){

    companion object DiffCallBack: DiffUtil.ItemCallback<GroupPlaylist>() {
        override fun areItemsTheSame(oldItem: GroupPlaylist, newItem: GroupPlaylist): Boolean {
            return oldItem.title==newItem.title
        }

        override fun areContentsTheSame(oldItem: GroupPlaylist, newItem: GroupPlaylist): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder private constructor
        (private val binding: ItemGroupPlaylistBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(groupPlaylist: GroupPlaylist, clickListener: PlaylistAdapter.OnClickListener){
            binding.groupPlaylist = groupPlaylist
            val playlistAdapter = PlaylistAdapter(PlaylistAdapter.OnClickListener {
                clickListener.onClick(it)
            })
            binding.playlists.adapter = playlistAdapter
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGroupPlaylistBinding.inflate(layoutInflater,parent,false)
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

}
