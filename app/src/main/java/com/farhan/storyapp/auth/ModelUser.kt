package com.farhan.storyapp.auth

data class ModelUser(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
