package com.malqaa.androidappp.newPhase.presentation.activities.loginScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _loginState = MutableLiveData<NetworkResponse<LoginResponse>>()
    val loginState: LiveData<NetworkResponse<LoginResponse>> = _loginState

    fun login(email: String, password: String, deviceId: String) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading
            _loginState.value = loginWebsiteUseCase(email, password, deviceId)
        }
    }
}