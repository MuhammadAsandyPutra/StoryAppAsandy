package com.example.storyappasandy.view

import com.example.storyappasandy.data.DataRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappasandy.inject.injectionClass
import com.example.storyappasandy.maps.MapsViewModel
import com.example.storyappasandy.view.camera.PostingViewModel
import com.example.storyappasandy.view.login.LoginViewModel
import com.example.storyappasandy.view.main.MainViewModel

class ViewModelFactory (private val repository: DataRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java)-> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PostingViewModel::class.java) -> {
                PostingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory{
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(injectionClass.provideRepository(context))

                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}