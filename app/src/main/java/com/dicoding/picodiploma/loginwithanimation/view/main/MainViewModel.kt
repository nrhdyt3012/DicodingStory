package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    // Flow for stories
    private val _storiesFlow = MutableStateFlow<List<ListStoryItem>>(emptyList())
    val storiesFlow: StateFlow<List<ListStoryItem>> = _storiesFlow

    // LiveData for loading status
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData for session token
    val token: LiveData<String> = getSessionToken()

    // LiveData for paging data (list of stories)
    val stories: LiveData<PagingData<ListStoryItem>> = token.switchMap { token ->
        liveData {
            if (token.isNotEmpty()) {
                // Set loading to true when fetching data
                _isLoading.value = true

                // Fetch stories from repository and cache them in the ViewModel scope
                try {
                    emitSource(repository.getPagingStories(token).cachedIn(viewModelScope))
                } catch (e: Exception) {
                    Log.e("MainViewModel", "Error fetching stories: ${e.message}")
                } finally {
                    // Set loading to false after fetching data
                    _isLoading.value = false
                }
            } else {
                Log.d("MainViewModel", "Token is empty, skipping data fetch")
            }
        }
    }

    init {
        // Get session information (user data including token)
        getSession()
    }

    // Retrieve session data (user model) to get the token
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    // Fetch token from session and update LiveData
    private fun getSessionToken(): LiveData<String> {
        val result = MutableLiveData<String>()
        viewModelScope.launch {
            repository.getSession().collect { user ->
                // Update the token in result LiveData
                result.postValue(user.token)
                Log.d("MainViewModel", "Token received: ${user.token}")
            }
        }
        return result
    }

    // Function to logout and clear session
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            Log.d("MainViewModel", "User logged out successfully")
        }
    }
}