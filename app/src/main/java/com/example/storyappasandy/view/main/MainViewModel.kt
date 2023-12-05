package com.example.storyappasandy.view.main
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappasandy.data.DataRepository
import com.example.storyappasandy.data.api.ListStoryItem
import com.example.storyappasandy.data.pref.DataModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataRepository): ViewModel() {

    val stories: LiveData<PagingData<ListStoryItem>>
    = repository.getStories().cachedIn(viewModelScope)

    fun getSession(): LiveData<DataModel> {
        return repository.getSession().asLiveData()
    }



    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}
