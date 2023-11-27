package com.farhan.storyapp.view.register

import androidx.lifecycle.ViewModel
import com.farhan.storyapp.auth.UserRepository

class RegisterViewModel (private val userRepository: UserRepository,)  : ViewModel()  {

    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)
}