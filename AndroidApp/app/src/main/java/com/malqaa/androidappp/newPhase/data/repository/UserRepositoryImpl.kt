package com.malqaa.androidappp.newPhase.data.repository

import com.casecode.domain.model.GeneralResponse
import com.casecode.domain.model.changeLanguage.ChangeLanguageResponse
import com.casecode.domain.model.changePassword.ChangePasswordRequest
import com.malqaa.androidappp.newPhase.data.api.OnRufApiService
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginRequest
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginResponse
import com.malqaa.androidappp.newPhase.domain.repository.UserRepository
import com.malqaa.androidappp.newPhase.domain.utils.NetworkResponse
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import com.yariksoffice.lingver.Lingver
import okhttp3.RequestBody

// Data Layer: Repository Implementation
class UserRepositoryImpl(
    private val onRufApiService: OnRufApiService
) : UserRepository {

    override suspend fun changeLanguage(language: String): NetworkResponse<ChangeLanguageResponse> {
        return onRufApiService.changeLanguage(language)
    }

    // Login function in the repository
    override suspend fun loginWebsite(loginRequest: LoginRequest): NetworkResponse<LoginResponse> {
        val map: HashMap<String, RequestBody> = HashMap()
        map["email"] = loginRequest.email.requestBody()
        map["password"] = loginRequest.password.requestBody()
        map["lang"] = loginRequest.lang.requestBody()
        map["deviceId"] = loginRequest.deviceId.requestBody()
        map["deviceType"] = loginRequest.deviceType.requestBody()

        val response = onRufApiService.loginWebsite(map)
        return response // Assuming you map the API response to a domain model
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
