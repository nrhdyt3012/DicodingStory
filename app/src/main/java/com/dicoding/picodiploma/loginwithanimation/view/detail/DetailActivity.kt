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
//        val eventOwner = intent.getStringExtra(EXTRA_EVENT_OWNER) ?: "Unknown"
//        val eventTime = intent.getStringExtra(EXTRA_EVENT_BEGIN_TIME) ?: "Unknown Time"
//        val eventQuota = intent.getIntExtra(EXTRA_EVENT_QUOTA, 0)
//        val eventImage = intent.getStringExtra(EXTRA_EVENT_IMAGE) ?: ""
//        val registerUrl = intent.getStringExtra(EXTRA_EVENT_REGISTER_URL) ?: ""

        binding.tvStoryName.text = storyName
        binding.tvStoryDescription.text = storyDescription
//        binding.tvBeginTime.text = getString(R.string.tv_begin_time, eventTime)
//        binding.tvQuota.text = getString(R.string.tv_quota, eventQuota)

        Glide.with(this)
            .load(storyImage)
//            .placeholder(R.drawable.placeholder_image)
//            .error(R.drawable.error_image)
            .into(binding.ivStoryImage)

//        binding.tvStoryDescription.text = HtmlCompat.fromHtml(eventDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)

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
//        const val EXTRA_STORY_CREATED_AT = "story_photo_url"
//        const val EXTRA_STORY_LATITUDE = "story_latitude"
//        const val EXTRA_STORY_LONGITUDE = "story_longitude"

    }
}

