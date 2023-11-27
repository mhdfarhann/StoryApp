package com.farhan.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.farhan.storyapp.auth.ModelUser
import com.farhan.storyapp.auth.RepositoryStory
import com.farhan.storyapp.auth.UserRepository
import kotlinx.coroutines.launch

class MainViewModel (
    private val userRepository: UserRepository,
    private val repositoryStory: RepositoryStory) : ViewModel()  {

    fun getSession(): LiveData<ModelUser> = userRepository.getLoginSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            userRepository.logoutSession()
        }
    }

    fun getStories(token: String) = repositoryStory.getStories(token)


}