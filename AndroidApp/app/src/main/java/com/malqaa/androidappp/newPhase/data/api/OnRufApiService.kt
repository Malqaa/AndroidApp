package com.malqaa.androidappp.newPhase.data.api

import com.casecode.domain.model.GeneralResponse
import com.casecode.domain.model.changeLanguage.ChangeLanguageResponse
import com.casecode.domain.model.changePassword.ChangePasswordRequest
import com.malqaa.androidappp.newPhase.domain.model.loginWebsite.LoginResponse
import com.malqaa.androidappp.newPhase.domain.utils.NetworkResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap
import retrofit2.http.Query

interface OnRufApiService {

    @POST("ChangeLanguage")
    suspend fun changeLanguage(
        @Query("language") language: String
    ): NetworkResponse<ChangeLanguageResponse>

    @Multipart
    @POST("loginWebsite")
    suspend fun loginWebsite(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>
    ): NetworkResponse<LoginResponse>

    @POST("ForgetPassword")
    suspend fun forgetPassword(
        @Query("email") email: String
    ): NetworkResponse<GeneralResponse>

    @POST("ChangePassword")
    suspend fun changePassword(
        @Body data: ChangePasswordRequest
    ): NetworkResponse<GeneralResponse>

}