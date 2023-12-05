package com.example.storyappasandy.inject

import com.example.storyappasandy.data.DataRepository
import android.content.Context
import com.example.storyappasandy.data.api.ApiConfig
import com.example.storyappasandy.data.pref.DataPreference
import com.example.storyappasandy.data.pref.dataStore

object injectionClass {

    fun provideRepository(context: Context): DataRepository {
        // Inisialisasi DataStore
        val dataStore = context.dataStore

        // Inisialisasi DataPreference
        val dataPreference = DataPreference.getInstance(dataStore)

        // Inisialisasi ApiService (belum memerlukan token)
        val apiService = ApiConfig.getApiService()

        return DataRepository.getInstance(dataPreference, apiService, dataStore)
    }

}