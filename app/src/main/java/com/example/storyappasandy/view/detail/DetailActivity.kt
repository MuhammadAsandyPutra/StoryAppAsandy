package com.example.storyappasandy.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyappasandy.data.api.ListStoryItem
import com.example.storyappasandy.data.utils.Utils
import com.example.storyappasandy.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()

        val details = intent.getParcelableExtra<ListStoryItem>("detail")
        if (details != null){
            binding.apply {
                Glide.with(this@DetailActivity)
                    .load(details.photoUrl)
                    .centerCrop()
                    .into(binding.IvDetail)
                nameDetail.text = details.name
                dateDetail.text = details.createdAt?.let { Utils.formatApiDateToDesiredFormat(it) }
                TvDetail.text = details.description
                authorDetail.text
        }
    }

    }

}