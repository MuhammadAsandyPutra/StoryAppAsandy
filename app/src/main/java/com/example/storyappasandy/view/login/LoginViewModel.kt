package com.example.storyappasandy.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappasandy.data.DataRepository
import com.example.storyappasandy.data.api.LoginResponse
import com.example.storyappasandy.data.api.LoginResult
import com.example.storyappasandy.data.pref.DataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> get() = _loginResponse

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    private val _userToken = MutableLiveData<String?>()
    val userToken: LiveData<String?> get() = _userToken

    private val _errorMessage = MutableLiveData<LoginResponse>()
    val errorMessage: LiveData<LoginResponse> = _errorMessage

    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = dataRepository.loginUser(email, password)
            _loginResponse.postValue(response)

            if (response.error == false) {
                val token = response.loginResult?.token ?: ""
                dataRepository.saveTokenToDataStore(token)


                val userToken = dataRepository.getTokenFromDataStore()
                dataRepository.simpanSession(DataModel(email, token))
                _userToken.postValue(userToken.toString())
            }else{
                val errorBody = response.message
                if (errorBody != null) {
                    if (errorBody.contains("Email belum terdaftar", ignoreCase = true)) {
                        errorMessage
                    }
                }
            }


        }
    }
}