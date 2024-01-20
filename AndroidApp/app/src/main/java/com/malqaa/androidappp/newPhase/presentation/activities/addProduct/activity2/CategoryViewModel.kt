package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity2

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.HttpException

class CategoryViewModel : BaseViewModel() {
    var isLoadingAllCategory: MutableLiveData<Boolean> = MutableLiveData()
    var categoriesObserver: MutableLiveData<GeneralResponse> = MutableLiveData()

    private var callAllCategories: Call<GeneralResponse>? = null

    fun closeAllCall() {
        if (callAllCategories != null) {
            callAllCategories?.cancel()
        }
    }

    fun getAllCategories() {
        isLoadingAllCategory.value = true

        callAllCategories = RetrofitBuilder.getRetrofitBuilder().getAllCategories()
        callApi(callAllCategories!!,
            onSuccess = {
                isLoadingAllCategory.value = false
                categoriesObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoadingAllCategory.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoadingAllCategory.value = false
                needToLogin.value = true
            })

    }
}