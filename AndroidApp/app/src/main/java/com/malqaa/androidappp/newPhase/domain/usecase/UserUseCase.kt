package com.malqaa.androidappp.newPhase.domain.usecase

import com.casecode.domain.model.GeneralResponse
import com.casecode.domain.model.changeLanguage.ChangeLanguageResponse
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginResponse
import com.malqaa.androidappp.newPhase.domain.repository.UserRepository
import com.malqaa.androidappp.newPhase.domain.utils.NetworkResponse

// Domain Layer: Use Cases
class ChangeLanguageUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(language: String): NetworkResponse<ChangeLanguageResponse> {
        return userRepository.changeLanguage(language)
    }
}

class LoginWebsiteUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        deviceId: String,
    ): NetworkResponse<LoginResponse> {
        return userRepository.loginWebsite(email, password, deviceId)
    }
}

class ForgetPasswordUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String): NetworkResponse<GeneralResponse> {
        return userRepository.forgetPassword(email)
    }
}

class ChangePasswordUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        email: String,
        resetPasswordCode: String,
        newPassword: String
    ): NetworkResponse<GeneralResponse> {
        return userRepository.changePassword(email, resetPasswordCode, newPassword)
    }
}
