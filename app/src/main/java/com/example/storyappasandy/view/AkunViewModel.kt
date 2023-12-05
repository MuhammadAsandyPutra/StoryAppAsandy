
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappasandy.data.DataRepository
import com.example.storyappasandy.data.api.ErrorResponse
import com.example.storyappasandy.data.api.LoginResponse
import com.example.storyappasandy.data.api.RegisterResponse
import com.google.gson.Gson
import retrofit2.HttpException

class AkunViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> get() = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> get() = _loginResponse

    private val _userToken = MutableLiveData<String>()
    val userToken: LiveData<String> get() = _userToken

    private val _errorResponse = MutableLiveData<String?>()
    val errorResponse: LiveData<String?> get() = _errorResponse

    init {
        // Di sini kita tidak lagi mencoba untuk mengambil token pada saat inisialisasi ViewModel.
        // Token akan diambil saat diperlukan.
    }

    suspend fun registerUser(name: String, email: String, password: String) {
        try {
            val response = dataRepository.registerUser(name, email, password)

            if (response.error == false) {
                _registerResponse.postValue(response)
            } else {
                val jsonInString = response.message
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _errorResponse.postValue(errorMessage)
            }
        } catch (e: HttpException) {
            val errorMessage = "Terjadi kesalahan saat menghubungi server"
            _errorResponse.postValue(errorMessage)
        } catch (e: Exception) {
            _errorResponse.postValue("Terjadi kesalahan: ${e.message}")
        }
    }

}
