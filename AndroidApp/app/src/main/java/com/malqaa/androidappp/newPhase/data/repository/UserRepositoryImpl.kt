package com.malqaa.androidappp.newPhase.data.repository

import com.casecode.domain.model.GeneralResponse
import com.casecode.domain.model.changeLanguage.ChangeLanguageResponse
import com.casecode.domain.model.changePassword.ChangePasswordRequest
import com.malqaa.androidappp.newPhase.data.api.OnRufApiService
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginRequest
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginResponse
import com.malqaa.androidappp.newPhase.domain.repository.UserRepository
import com.malqaa.androidappp.newPhase.domain.utils.NetworkResponse
import com.yariksoffice.lingver.Lingver

// Data Layer: Repository Implementation
class UserRepositoryImpl(
    private val onRufApiService: OnRufApiService
) : UserRepository {

    override suspend fun changeLanguage(language: String): NetworkResponse<ChangeLanguageResponse> {
        return onRufApiService.changeLanguage(language)
    }

    override suspend fun loginWebsite(
        email: String,
        password: String,
        deviceId: String
    ): NetworkResponse<LoginResponse> {
        val loginRequest = LoginRequest(
            email = email,
            password = password,
            lang = Lingver.getInstance().getLanguage(),
            deviceId = deviceId,
            deviceType = DEVICE_TYPE
        )
        return onRufApiService.loginWebsite(loginRequest.toPartMap())
    }

    override suspend fun forgetPassword(email: String): NetworkResponse<GeneralResponse> {
        return onRufApiService.forgetPassword(email)
    }

    override suspend fun changePassword(
        email: String,
        resetPasswordCode: String,
        newPassword: String
    ): NetworkResponse<GeneralResponse> {
        val request = ChangePasswordRequest(email, resetPasswordCode, newPassword)
        return onRufApiService.changePassword(request)
    }

    companion object {
        const val DEVICE_TYPE = "Android"
    }
}
