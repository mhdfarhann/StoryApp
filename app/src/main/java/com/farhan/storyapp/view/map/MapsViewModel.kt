package com.farhan.storyapp.view.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.farhan.storyapp.auth.ModelUser
import com.farhan.storyapp.auth.RepositoryStory
import com.farhan.storyapp.auth.UserRepository
import com.farhan.storyapp.data.response.ListStory
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel (
    private val userRepository: UserRepository,
    private val storyRepository: RepositoryStory
) : ViewModel() {
    private val _storyResponse = MutableLiveData<ListStory>()
    val storyResponse: LiveData<ListStory> = _storyResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<ModelUser> = userRepository.getLoginSession().asLiveData()

    fun getStoriesLocation(token: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = storyRepository.getStoriesLocation(token)
                Log.d(TAG, "onSuccess")
                _isLoading.postValue(false)
                _storyResponse.postValue(response.body())
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ListStory::class.java)
                val errorMassage = errorBody.message
                _isLoading.postValue(false)
                Log.d(TAG, "onError: $errorMassage")
            }
        }
    }
    companion object {
        private const val TAG = "MainViewModel"
    }

}