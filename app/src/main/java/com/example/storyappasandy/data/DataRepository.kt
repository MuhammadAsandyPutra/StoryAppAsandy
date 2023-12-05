package com.example.storyappasandy.data
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyappasandy.data.api.ApiService
import com.example.storyappasandy.data.api.FileUploadResponse
import com.example.storyappasandy.data.api.ListStoryItem
import com.example.storyappasandy.data.api.LoginResponse
import com.example.storyappasandy.data.api.RegisterResponse
import com.example.storyappasandy.data.api.StoryListResponse
import com.example.storyappasandy.data.pref.DataModel
import com.example.storyappasandy.data.pref.DataPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class DataRepository private constructor(
    private val userPreference: DataPreference,
    private var apiService: ApiService,
    private val dataStore: DataStore<Preferences>,
    private var token: String? = null

) {

    companion object {
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(
            userPreference: DataPreference,
            apiService: ApiService,
            dataStore: DataStore<Preferences>
        ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(userPreference, apiService, dataStore)
            }.also { instance = it }
    }

    suspend fun simpanSession(user: DataModel) {
        userPreference.simpanSession(user)
    }

    fun getSession(): Flow<DataModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getStoriesWithLocation(): StoryListResponse {
        val token = getTokenFromDataStore()
        return if (token != null) {
            apiService.getStoriesWithLocation("Bearer $token")
        } else {
            StoryListResponse(emptyList(), true, "Token tidak tersedia")
        }


    }

    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {
        try {
            val response = apiService.register(name, email, password)
            if (response.error == false) {

                return response
            } else {

                val errorMessage = response.message ?: "Pendaftaran gagal"
                return RegisterResponse(error = true, message = errorMessage)
            }
        } catch (e: Exception) {

            return RegisterResponse(error = true, message = "Terjadi kesalahan saat menghubungi server")
        }
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        try {
            val response = apiService.login(email, password)
            if (response.error == false) {

                val loginResult = response.loginResult
                val token = loginResult?.token ?: ""
                saveTokenToDataStore(token)

                Log.d("LoginToken", "Token: $token")
            } else {

                val errorMessage = response.message ?: "Login gagal"
                return LoginResponse(error = true, message = errorMessage)
            }
            return response
        } catch (e: Exception) {

            return LoginResponse(error = true, message = "Akun anda tidak valid")
        }
    }

    suspend fun saveTokenToDataStore(token: String) {
        val tokenKey = stringPreferencesKey("user_token")
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }


//    suspend fun getStories(): StoryListResponse {
//        val token = getTokenFromDataStore()
//
//        return if (token != null) {
//            apiService.getStories("Bearer $token")
//        } else {
//
//            StoryListResponse(emptyList(), true, "Token tidak tersedia")
//        }
//    }

//    fun getStories(): LiveData<PagingData<ListStoryItem>> {
//        val token = getTokenFromDataStore()
//
//        return Pager(
//            config = PagingConfig(
//                pageSize = 20,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { StoryPagingSource(apiService, "Bearer $token") }
//        ).liveData
//    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        val tokenLiveData: LiveData<String?> = dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey("user_token")]
            }
            .asLiveData()

        return tokenLiveData.switchMap { token ->
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { StoryPagingSource(apiService, "Bearer $token") }
            ).liveData
        }
    }

    suspend fun getTokenFromDataStore(): String? {
        val tokenKey = stringPreferencesKey("user_token")
        return dataStore.data.first()[tokenKey]
    }




    fun uploadImage(imageFile: File, description: String, lat: Float, lon: Float) = liveData {
        val token = getTokenFromDataStore()
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())

        val latRequestBody = lat.toString().toRequestBody("text/plain".toMediaType())
        val lonRequestBody = lon.toString().toRequestBody("text/plain".toMediaType())

        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage("Bearer $token",multipartBody, requestBody, latRequestBody, lonRequestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }

    }

}