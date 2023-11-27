package com.farhan.storyapp.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.farhan.storyapp.data.API.ApiService
import com.farhan.storyapp.data.response.AddStoryResponse
import com.farhan.storyapp.data.response.Story
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File


class RepositoryStory private constructor(
    private val apiService: ApiService
) {
    fun getStories(token: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    suspend fun getStoriesLocation(token: String) = apiService.getStoriesWithLoc(token)

    fun addNewStory(token: String, description: String, imageFile: File): LiveData<Result<Story?>> {
        val responseLiveData: MutableLiveData<Result<Story?>> = MutableLiveData()
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        responseLiveData.value = Result.Loading

        try {
            apiService.addNewStory("Bearer $token", requestBody, multipartBody).enqueue(object :
                Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        responseLiveData.value = Result.Success(null)
                    } else {
                        responseLiveData.value = Result.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    responseLiveData.value = Result.Error(t.message.toString())
                }

            })
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, Error::class.java)
            responseLiveData.value = Result.Error(errorResponse.message.toString())
        }
        return responseLiveData
    }

    companion object {
        @Volatile
        private var instance: RepositoryStory? = null

        fun getInstance(apiService: ApiService): RepositoryStory =
            instance ?: synchronized(this) {
                instance ?: RepositoryStory(apiService)
            }.also { instance = it }
    }
}