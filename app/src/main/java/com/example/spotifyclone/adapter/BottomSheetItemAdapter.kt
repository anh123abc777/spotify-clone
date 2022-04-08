package com.example.spotifyclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tonezone.databinding.ItemBottomSheetBinding
import com.example.spotifyclone.utils.Signal

class BottomSheetItemAdapter(private val clickListener: OnClickListener) : ListAdapter<Signal, BottomSheetItemAdapter.ViewHolder>(
    DiffCallBack
){

    companion object DiffCallBack: DiffUtil.ItemCallback<Signal>() {
        override fun areItemsTheSame(oldItem: Signal, newItem: Signal): Boolean {
            return oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Signal, newItem: Signal): Boolean {
            return oldItem.name == newItem.name
        }

    }

    class ViewHolder private constructor
        (private val binding: ItemBottomSheetBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(signal: Signal, clickListener: OnClickListener){
            binding.signal = signal
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBottomSheetBinding.inflate(layoutInflater,parent,false)
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

    class OnClickListener(val clickListener : (signal: Signal) -> Unit) {

        fun onClick(signal: Signal) = clickListener(signal)
    }
}
