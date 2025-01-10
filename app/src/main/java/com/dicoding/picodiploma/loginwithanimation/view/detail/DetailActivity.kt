package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var eventId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventId = intent.getIntExtra(EXTRA_STORY_ID, 0)
        val storyName = intent.getStringExtra(EXTRA_STORY_NAME) ?: "No Title"
        val storyDescription = intent.getStringExtra(EXTRA_STORY_DESCRIPTION) ?: "No Description"
        val storyImage = intent.getStringExtra(EXTRA_STORY_PHOTO_URL) ?: "No Photo"


        binding.tvStoryName.text = storyName
        binding.tvStoryDescription.text = storyDescription


        Glide.with(this)
            .load(storyImage)
            .into(binding.ivStoryImage)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_STORY_ID = "story_id"
        const val EXTRA_STORY_NAME = "story_name"
        const val EXTRA_STORY_DESCRIPTION = "story_description"
        const val EXTRA_STORY_PHOTO_URL = "story_photo_url"


    }
}

