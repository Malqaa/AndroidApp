package com.malqaa.androidappp.newPhase.presentation.activities.loginScreen

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.AddFavResponse
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.yariksoffice.lingver.Lingver
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.HttpException

class LoginViewModel : BaseViewModel() {
    var userLoginObserver: MutableLiveData<LoginResp> = MutableLiveData()
    var changePasswordAfterForgetObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var forgetPasswordObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var languageObserver: MutableLiveData<AddFavResponse> = MutableLiveData()

    private var callChangePassword: Call<GeneralResponse>? = null
    private var callLogin: Call<LoginResp>? = null
    private var callForgetPassword: Call<GeneralResponse>? = null
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

    fun signInUser(email: String, pass: String, deviceId: String) {
        isLoading.value = true

        val map: HashMap<String, RequestBody> = HashMap()
        map["email"] = email.requestBody()
        map["password"] = pass.requestBody()
        map["lang"] = Lingver.getInstance().getLanguage().requestBody()
        map["deviceId"] = deviceId.requestBody()
        map["deviceType"] = HelpFunctions.deviceType.requestBody()

        callLogin = getRetrofitBuilder().loginUser(map)

        callApi(callLogin!!,
            onSuccess = {
                isLoading.value = false
                userLoginObserver.value = it
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

    fun forgetPassword(email: String) {
        isLoading.value = true
        callForgetPassword = getRetrofitBuilder().forgetPassword(email)
        callApi(callForgetPassword!!,
            onSuccess = {
                isLoading.value = false
                forgetPasswordObserver.value = it
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

    fun changePasswordAfterForget(email: String, otpCOde: String, password: String) {
        isLoading.value = true
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["email"] = email
        hashMap["resetPasswordCode"] = otpCOde
        hashMap["newPassword"] = password
        callChangePassword = getRetrofitBuilder().changePasswordAfterForget(hashMap)
        callApi(callChangePassword!!,
            onSuccess = {
                isLoading.value = false
                changePasswordAfterForgetObserver.value = it
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

    var isLoadingAssignCartToUser: MutableLiveData<Boolean> = MutableLiveData()
    private var callAssignCardToUser: Call<GeneralResponse>? = null
    var assignCartToUserObserver: MutableLiveData<GeneralResponse> = MutableLiveData()

    fun assignCardToUser(masterCartId: String) {
        isLoadingAssignCartToUser.value = true
        callAssignCardToUser = getRetrofitBuilder().assignCartMastetToUser(masterCartId)
        callApi(callAssignCardToUser!!,
            onSuccess = {
                isLoadingAssignCartToUser.value = false
                assignCartToUserObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoadingAssignCartToUser.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoadingAssignCartToUser.value = false
                needToLogin.value = true
            })
    }

    fun closeAllCall() {
        if (callChangePassword != null) {
            callChangePassword?.cancel()
        }
        if (callLogin != null) {
            callLogin?.cancel()
        }
        if (callForgetPassword != null) {
            callForgetPassword?.cancel()
        }
        if (changeLanguage != null) {
            changeLanguage?.cancel()
        }
        if (callAssignCardToUser != null) {
            callAssignCardToUser?.cancel()
        }
    }

}