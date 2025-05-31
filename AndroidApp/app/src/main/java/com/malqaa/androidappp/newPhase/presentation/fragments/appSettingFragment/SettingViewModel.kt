package com.malqaa.androidappp.newPhase.presentation.fragments.appSettingFragment

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.AddFavResponse
import retrofit2.Call
import retrofit2.HttpException

class SettingViewModel : BaseViewModel() {
    var languageObserver: MutableLiveData<AddFavResponse> = MutableLiveData()
    private var changeLanguage: Call<AddFavResponse>? = null


    fun setLanguageChange(language: String) {
        isLoading.value = true
        changeLanguage = RetrofitBuilder.getRetrofitBuilder().changeLanguage(language)
        callApi(changeLanguage!!,
            onSuccess = {
                isLoading.value = false
                languageObserver.value = it
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
        if (changeLanguage != null) {
            changeLanguage?.cancel()
        }
    }
}