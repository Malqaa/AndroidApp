package com.malqaa.androidappp.newPhase.domain.repository

import com.casecode.domain.model.GeneralResponse
import com.casecode.domain.model.changeLanguage.ChangeLanguageResponse
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginResponse
import com.malqaa.androidappp.newPhase.domain.utils.NetworkResponse

// Domain Layer: Repository Interface
interface UserRepository {
    suspend fun changeLanguage(language: String): NetworkResponse<ChangeLanguageResponse>

    suspend fun loginWebsite(
        email: String,
        password: String,
        deviceId: String
    ): NetworkResponse<LoginResponse>

    suspend fun forgetPassword(email: String): NetworkResponse<GeneralResponse>

    suspend fun changePassword(
        email: String,
        resetPasswordCode: String,
        newPassword: String
    ): NetworkResponse<GeneralResponse>
}
