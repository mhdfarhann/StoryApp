package com.farhan.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.farhan.storyapp.auth.RepositoryStory
import com.farhan.storyapp.auth.UserRepository
import com.farhan.storyapp.di.Injection
import com.farhan.storyapp.view.addStory.AddStoryViewModel
import com.farhan.storyapp.view.login.LoginViewModel
import com.farhan.storyapp.view.main.MainViewModel
import com.farhan.storyapp.view.map.MapsViewModel
import com.farhan.storyapp.view.register.RegisterViewModel

class ViewModelFactory (private val userRepository: UserRepository, private val repositoryStory: RepositoryStory) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, repositoryStory ) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
               LoginViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repositoryStory, userRepository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(userRepository, repositoryStory) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory{
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideStoryRepository()
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}