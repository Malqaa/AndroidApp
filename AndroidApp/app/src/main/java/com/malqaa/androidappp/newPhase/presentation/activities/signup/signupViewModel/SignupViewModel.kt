package com.malqaa.androidappp.newPhase.presentation.activities.signup.signupViewModel


import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.countryResp.CountriesResp
import com.malqaa.androidappp.newPhase.domain.models.resgisterResp.RegisterResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.AddFavResponse
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.UserVerifiedResp
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.ValidateAndGenerateOTPResp
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import com.malqaa.androidappp.newPhase.utils.helper.ConstantsHelper.getMultiPart
import retrofit2.Call
import retrofit2.HttpException
import java.io.File


class SignupViewModel : BaseViewModel() {

    var validateAndGenerateOTPObserver: MutableLiveData<ValidateAndGenerateOTPResp> =
        MutableLiveData()
    var userVerifiedObserver: MutableLiveData<UserVerifiedResp> = MutableLiveData()
    var registerRespObserver: MutableLiveData<RegisterResp> = MutableLiveData()
    var countryResp: MutableLiveData<CountriesResp> = MutableLiveData()
    var languageObserver: MutableLiveData<AddFavResponse> = MutableLiveData()

    private var callCreate: Call<RegisterResp>? = null
    private var callVerify: Call<ValidateAndGenerateOTPResp>? = null
    private var callResend: Call<ValidateAndGenerateOTPResp>? = null
    private var callVerifyOtp: Call<UserVerifiedResp>? = null
    private var changeLanguage: Call<AddFavResponse>? = null

    fun setLanguageChange(language: String) {
        isLoading.value = true
        changeLanguage = getRetrofitBuilder().changeLanguage(language)
        callApi(changeLanguage!!,
            onSuccess = {
                isLoading.value = false
                languageObserver.value = it
            },
            onFailure = { throwable, _, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun validateUserAndGenerateOTP(
        userName: String,
        email: String,
        fullNumberWithPlus: String,
        language: String
    ) {
        isLoading.value = true
        callVerify = getRetrofitBuilder().validateUserAndGenerateOtp(
            userName,
            email,
            fullNumberWithPlus
        )
        callApi(callVerify!!,
            onSuccess = {
                isLoading.value = false
                validateAndGenerateOTPObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }


    fun resendOtp(userPhone: String, language: String, otpType: String) {
        isLoading.value = true
        callResend = getRetrofitBuilder().resendOtp(userPhone, otpType, language)
        callApi(callResend!!,
            onSuccess = {
                isLoading.value = false
                validateAndGenerateOTPObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun verifyOtp(userPhone: String, otpCode: String) {
        isLoading.value = true
        callVerifyOtp = getRetrofitBuilder().verifyOtp(userPhone, otpCode)
        callApi(callVerifyOtp!!,
            onSuccess = {
                isLoading.value = false
                userVerifiedObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun createUser(
        userName: String,
        phoneNumber: String,
        userEmail: String,
        userPass: String,
        invitationCode: String,
        firstName: String,
        editTextLastname: String,
        date: String,
        gender_: String,
        selectedCountryId: String,
        selectedRegionId: String,
        cityId: String,
        area: String,
        streetNUmber: String,
        county_code: String,
        isBusinessAccount: String,
        lang: String,
        projectName: String,
        deviceType: String,
        deviceId: String,
        file: File?
    ) {
        isLoading.value = true
        val multipartBody = getMultiPart(file, "image/*", "imgProfile")
        callCreate = getRetrofitBuilder()
            .createUser2(
                userName.requestBody(),
                phoneNumber.requestBody(),
                userEmail.requestBody(),
                userPass.requestBody(),
                invitationCode.requestBody(),
                firstName.requestBody(),
                editTextLastname.requestBody(),
                date.requestBody(),
                gender_.requestBody(),
                selectedCountryId.requestBody(),
                selectedRegionId.requestBody(),
                cityId.requestBody(),
                area.requestBody(),
                streetNUmber.requestBody(),
                county_code.requestBody(),
                isBusinessAccount.requestBody(),
                lang.requestBody(),
                projectName.requestBody(),
                deviceType.requestBody(),
                deviceId.requestBody(),
                multipartBody
            )
        callApi(callCreate!!,
            onSuccess = {
                isLoading.value = false
                registerRespObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun closeAllCall() {
        if (callVerify != null) {
            callVerify?.cancel()
        }
        if (callVerifyOtp != null) {
            callVerifyOtp?.cancel()
        }
        if (callCreate != null) {
            callCreate?.cancel()
        }
        if (callResend != null) {
            callResend?.cancel()
        }
        if (changeLanguage != null)
            changeLanguage?.cancel()

    }
}