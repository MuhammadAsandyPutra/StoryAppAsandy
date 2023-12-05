package com.example.storyappasandy.maps

import com.example.storyappasandy.data.DataRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappasandy.data.api.StoryListResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: DataRepository): ViewModel() {


    private val locationStoriesLiveData = MutableLiveData<StoryListResponse>()
    val locationStories: LiveData<StoryListResponse> get() = locationStoriesLiveData

    init {
        viewModelScope.launch {
            val user = repository.getSession().first()
            if (user.isLogin) {
                fetchLocationStories()
            }
        }
    }


    suspend fun fetchLocationStories() {
        val response = repository.getStoriesWithLocation()
        locationStoriesLiveData.postValue(response)
    }
}