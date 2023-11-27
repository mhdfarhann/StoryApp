package com.farhan.storyapp.view.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.farhan.storyapp.DataDummy
import com.farhan.storyapp.auth.Result
import com.farhan.storyapp.auth.UserRepository
import com.farhan.storyapp.data.response.Register
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
class RegisterViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var registerViewModel: RegisterViewModel
    private val dummySignUpResponse = DataDummy.generateDummyRegister()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(userRepository)
    }

    @Test
    fun `when register Should Not Null and return success`() {
        val expectedRegisterResponse = MutableLiveData<Result<Register?>>()
        expectedRegisterResponse.value = Result.Success(dummySignUpResponse)
        val name = "name"
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(userRepository.register(name, email, password)).thenReturn(expectedRegisterResponse)

        val actualResponse = registerViewModel.register(name, email, password).getOrAwaitValue()
        Mockito.verify(userRepository).register(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedRegisterResponse = MutableLiveData<Result<Register?>>()
        expectedRegisterResponse.value = Result.Error("network error")
        val name = "name"
        val email = "name@email.com"
        val password = "secretpassword"

        Mockito.`when`(userRepository.register(name, email, password)).thenReturn(expectedRegisterResponse)

        val actualResponse = registerViewModel.register(name, email, password).getOrAwaitValue()
        Mockito.verify(userRepository).register(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}