package com.example.spotifyclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tonezone.databinding.ItemTrackBinding
import com.example.spotifyclone.network.Track

class TrackAdapter(private val clickListener: OnClickListener) : ListAdapter<Track, TrackAdapter.ViewHolder>(
    DiffCallBack
){

    companion object DiffCallBack: DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.name == newItem.name
        }

    }

    class ViewHolder private constructor
        (private val binding: ItemTrackBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track, clickListener: OnClickListener){
            binding.track = track
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTrackBinding.inflate(layoutInflater,parent,false)
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

    class OnClickListener(val clickListener : (track: Track) -> Unit) {

        fun onClick(track: Track) = clickListener(track)
    }
}
