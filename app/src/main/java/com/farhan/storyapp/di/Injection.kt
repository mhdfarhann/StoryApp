package com.farhan.storyapp.di

import android.content.Context
import com.farhan.storyapp.auth.RepositoryStory
import com.farhan.storyapp.auth.UserRepository
import com.farhan.storyapp.data.API.ApiConfig

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val pref = SettingPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.apiInstance
        return UserRepository.getInstance(apiService,pref)
    }

    fun provideStoryRepository(): RepositoryStory {
        val apiService = ApiConfig.apiInstance
        return RepositoryStory.getInstance(apiService)
    }
}