package com.malqaa.androidappp.newPhase.presentation.activities.loginScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginRequest
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginResponse
import com.malqaa.androidappp.newPhase.domain.usecase.LoginWebsiteUseCase
import com.malqaa.androidappp.newPhase.domain.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel1 @Inject constructor(
    private val loginWebsiteUseCase: LoginWebsiteUseCase
) : ViewModel() {

    // Separate LiveData for each state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _retryMessage = MutableLiveData<String?>()
    val retryMessage: LiveData<String?> = _retryMessage

    // Function to handle login
    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val response = loginWebsiteUseCase(loginRequest)) {
                is NetworkResponse.Loading -> {
                    _isLoading.value = true
                }

                is NetworkResponse.Success -> {
                    _isLoading.value = false
                    _loginResponse.value = response.data
                }

                is NetworkResponse.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = response.message
                }

                is NetworkResponse.Retry -> {
                    _isLoading.value = false
                    _retryMessage.value = response.message
                }

                else -> {
                    _isLoading.value = false
                    // Handle other states if needed
                }
            }
        }
    }

    // Optionally, you can add functions to reset states after consumption
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearRetryMessage() {
        _retryMessage.value = null
    }
}
