package com.dicoding.picodiploma.loginwithanimation.view.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.api.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult = _loginResult

    private val _getToken = MutableStateFlow<String?>("")

    val getToken = _getToken

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Flag untuk menandakan login selesai
    private val _isLoginComplete = MutableLiveData<Boolean>(false)
    val isLoginComplete: LiveData<Boolean> = _isLoginComplete

    // Save user session to preferences
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    // Fungsi untuk login
    fun login(email: String, password: String) {
        _isLoading.value = true // Indikasi loading

        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val successResponse = apiService.login(email, password)
                _loginResult.value = successResponse.message
                val token = successResponse.loginResult.token
                _getToken.value = token
                // Simpan session setelah login sukses
                saveSession(UserModel(email,token))

                // Tandai login selesai
                _isLoginComplete.value = true
                Log.d(TAG, "LoginViewModel successResponse: ${successResponse.message}")
                Log.d(TAG, "LoginViewModel successResult: ${successResponse.loginResult}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                _loginResult.value = errorResponse.message
            } finally {
                _isLoading.value = false // Sembunyikan loading
            }
        }
    }

    // Reset status setelah login selesai
    fun resetLoginCompleteState() {
        _isLoginComplete.value = false
    }
}
