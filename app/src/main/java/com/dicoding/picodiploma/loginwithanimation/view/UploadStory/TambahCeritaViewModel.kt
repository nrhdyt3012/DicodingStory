package com.dicoding.picodiploma.loginwithanimation.view.UploadStory

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.api.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class TambahCeritaViewModel(private val UserRepository: UserRepository) : ViewModel() {
    private val _result = MutableStateFlow("")
    val result = _result
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    init {
        getSession()
    }
    fun getSession(): LiveData<UserModel> {
        return UserRepository.getSession().asLiveData()
    }
    suspend fun uploadImage(
        token: String,
        imageFile: MultipartBody.Part,
        requestBody: RequestBody
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val successResponse = ApiConfig.getApiService().uploadImage("Bearer $token", imageFile, requestBody)
                _result.value = successResponse.message.toString()
                Log.d(TAG, "uploadImage sucess: ${successResponse.message}")
            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                _result.value = errorResponse.message
                Log.d(TAG, "uploadImage fail: ${errorResponse.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}