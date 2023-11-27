package com.farhan.storyapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farhan.storyapp.auth.ModelUser
import com.farhan.storyapp.auth.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel (private val userRepository: UserRepository,)  : ViewModel() {

    fun login(email: String, password: String) = userRepository.login(email, password)

    fun setlogin(user: ModelUser) {
        viewModelScope.launch {
            userRepository.saveLoginSession(user)
        }
    }
}