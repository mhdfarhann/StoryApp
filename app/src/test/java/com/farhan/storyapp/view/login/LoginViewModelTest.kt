package com.farhan.storyapp.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.farhan.storyapp.DataDummy
import com.farhan.storyapp.auth.Result
import com.farhan.storyapp.auth.UserRepository
import com.farhan.storyapp.data.response.Login
import com.farhan.storyapp.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLogin()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `when login Should Not Null and return success`() {
        val expectedLoginResponse = MutableLiveData<Result<Login?>>()
        expectedLoginResponse.value = Result.Success(dummyLoginResponse)
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(userRepository.login(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(userRepository).login(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedLoginResponse = MutableLiveData<Result<Login?>>()
        expectedLoginResponse.value = Result.Error("network error")
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(userRepository.login(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(userRepository).login(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }

}