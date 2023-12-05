package com.example.storyappasandy.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappasandy.data.api.ListStoryItem
import com.example.storyappasandy.data.utils.Utils
import com.example.storyappasandy.databinding.StoryItemBinding
import com.example.storyappasandy.view.detail.DetailActivity

class StoryListAdapter : PagingDataAdapter<ListStoryItem, StoryListAdapter.StoryViewHolder>(StoryDiffCallback) {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
        holder.itemView.setOnClickListener {
            if (story != null) {
                onItemClickListener?.onItemClick(story)
            }
            val intent= Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("detail", story)

        }
    }

    inner class StoryViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .centerCrop()
                    .into(binding.storyImage)
                storyUsername.text = story.name
                storyTitle.text = story.createdAt?.let { Utils.formatApiDateToDesiredFormat(it) }
                storyContent.text = story.description

            }
        }
    }


    companion object {
        val StoryDiffCallback= object: DiffUtil.ItemCallback<ListStoryItem>(){

        override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem == newItem
         }

        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
    }

    interface OnItemClickListener{
        fun onItemClick(user: ListStoryItem){

        }
    }


}

