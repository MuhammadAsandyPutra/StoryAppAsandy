package com.example.storyappasandy.view.camera

import com.example.storyappasandy.data.DataRepository
import androidx.lifecycle.ViewModel
import java.io.File

class PostingViewModel (private val repository: DataRepository) : ViewModel() {
    fun uploadImage(file: File, description: String, lon: Float, lat: Float) = repository.uploadImage(file, description, lon, lat)
}