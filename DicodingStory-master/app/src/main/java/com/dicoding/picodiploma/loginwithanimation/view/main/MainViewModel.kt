package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.StoryPagingSource
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.api.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storiesFlow = MutableStateFlow<List<ListStoryItem>>(emptyList())
    val storiesFlow: StateFlow<List<ListStoryItem>> = _storiesFlow

    val stories: LiveData<PagingData<ListStoryItem>> =
        repository.getPagingStories().cachedIn(viewModelScope)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    init {
        getSession()
    }

    // Fetch Stories only if session and token are valid
    fun fetchStories(token: String) {
        viewModelScope.launch {
            if (token.isNotEmpty()) {
                _isLoading.value = true
                try {
                    val apiService = ApiConfig.getApiService()
                    val response = apiService.getStories("Bearer $token")
                    _storiesFlow.value = response.listStory
                    Log.d(TAG, "Stories fetched successfully")
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    Log.e(TAG, "Error fetching stories: ${errorResponse.message}")
                } finally {
                    _isLoading.postValue(false)
                }
            } else {
                Log.e(TAG, "Token is empty or invalid")
                _isLoading.postValue(false)
            }
        }
    }

    // Retrieve session data
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    // Logout user and clear session
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
