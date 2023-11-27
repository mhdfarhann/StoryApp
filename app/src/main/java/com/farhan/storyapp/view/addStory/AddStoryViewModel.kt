package com.farhan.storyapp.view.addStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.farhan.storyapp.auth.ModelUser
import com.farhan.storyapp.auth.RepositoryStory
import com.farhan.storyapp.auth.UserRepository
import java.io.File

class AddStoryViewModel (private val repositoryStory: RepositoryStory, private val userRepository: UserRepository) : ViewModel() {

    fun addNewStory(token: String, description: String, photo: File) =
        repositoryStory.addNewStory(token, description, photo)

    fun getSession(): LiveData<ModelUser> = userRepository.getLoginSession().asLiveData()
}