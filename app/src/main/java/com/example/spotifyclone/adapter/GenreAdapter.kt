package com.example.spotifyclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifyclone.network.Category
import com.example.tonezone.databinding.ItemGenreBinding

class GenreAdapter(val clickListener: OnClickListener): ListAdapter<Category, GenreAdapter.ViewHolder>(
    DiffCallBack
) {

    companion object DiffCallBack: DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return  oldItem == newItem
        }
    }

    class ViewHolder
    private constructor
        (private val binding: ItemGenreBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, clickListener: OnClickListener){
            binding.category = category
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGenreBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),clickListener)
    }

    override fun getItemCount(): Int {
        return currentList.size.coerceAtMost(20)
    }
    class OnClickListener(val clickListener : (category: Category) -> Unit) {

        fun onClick(category: Category) = clickListener(category)
    }
}
