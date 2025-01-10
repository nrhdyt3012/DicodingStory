package com.dicoding.picodiploma.loginwithanimation.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storyName: TextView = view.findViewById(R.id.tvStoryName)
        val storyImage: ImageView = view.findViewById(R.id.ivStoryImage)
        val publishedDate: TextView = view.findViewById(R.id.tvPublishedDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position) // Get item from PagingDataAdapter
        Log.d("AdapterData", "Story: $story")
        if (story != null) {
            holder.storyName.text = story.name
            holder.publishedDate.text = story.createdAt

            Glide.with(holder.itemView.context)
                .load(story.photoUrl)
                .into(holder.storyImage)

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY_NAME, story.name)
                intent.putExtra(DetailActivity.EXTRA_STORY_ID, story.id)
                intent.putExtra(DetailActivity.EXTRA_STORY_DESCRIPTION, story.description)
                intent.putExtra(DetailActivity.EXTRA_STORY_PHOTO_URL, story.photoUrl)
                context.startActivity(intent)
            }
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
