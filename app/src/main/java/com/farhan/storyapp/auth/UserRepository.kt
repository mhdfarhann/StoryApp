package com.farhan.storyapp.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.farhan.storyapp.data.API.ApiService
import com.farhan.storyapp.data.response.Login
import com.farhan.storyapp.data.response.Register
import com.farhan.storyapp.di.SettingPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val SettingPreferences: SettingPreferences
) {
    fun register(name: String, email: String, password: String): MutableLiveData<Result<Register?>> {
        val responseLiveData: MutableLiveData<Result<Register?>> = MutableLiveData()
        responseLiveData.value = Result.Loading
        try {
            apiService.doRegister(name, email, password).enqueue(object : Callback<Register> {
                override fun onResponse(call: Call<Register>, response: Response<Register>) {
                    if(response.isSuccessful){
                        responseLiveData.value  = Result.Success(response.body())
                    }else{
                        responseLiveData.value = Result.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<Register>, t: Throwable) {
                    responseLiveData.value = Result.Error(t.message.toString())
                }

            })
        } catch (e: HttpException){

            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, Error::class.java)
            responseLiveData.value = errorResponse.message?.let { Result.Error(it) }
        }
        return responseLiveData
    }

    fun login(email: String, password: String): MutableLiveData<Result<Login?>> {
        val responseLiveData: MutableLiveData<Result<Login?>> = MutableLiveData()
        responseLiveData.value = Result.Loading
        try {
            apiService.doLogin(email, password).enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    Log.wtf("RESPONSE", response.isSuccessful.toString())
                    if(response.isSuccessful){
                        responseLiveData.value  = Result.Success(response.body())
                    }else{
                        responseLiveData.value = Result.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    responseLiveData.value = Result.Error(t.message.toString())
                }

            })
        } catch (e: HttpException){

            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, Error::class.java)
            responseLiveData.value = errorResponse.message?.let { Result.Error(it) }
        }
        return responseLiveData
    }

    suspend fun saveLoginSession(user: ModelUser){
        SettingPreferences.saveLoginSession(user)
    }

    fun getLoginSession(): Flow<ModelUser> {
        return SettingPreferences.getLoginSession()
    }

    suspend fun logoutSession(){
        SettingPreferences.LogoutSession()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService,userPreference: SettingPreferences) : UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService,userPreference)
            }.also { instance = it }
    }


}