package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity7

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionResp
import retrofit2.Call


class ShippingViewModel : BaseViewModel() {
    var shippingListObserver: MutableLiveData<ShippingOptionResp> =
        MutableLiveData<ShippingOptionResp>()
    private var callAllShippingOptions: Call<ShippingOptionResp>? =null

    fun closeAllCall() {
        if (callAllShippingOptions != null) {
            callAllShippingOptions?.cancel()
        }
    }
    fun getAllShippingOptions() {
        //isLoading.value = true

        callAllShippingOptions = getRetrofitBuilder().getAllShippingOptions()
        callApi(callAllShippingOptions!!,
            onSuccess = {
//                isLoading.value = false
                shippingListObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
//                if (throwable != null && errorBody == null)
//                    isNetworkFail.value = throwable !is HttpException
//                else {
//                    errorResponseObserver.value =
//                        getErrorResponse(statusCode, errorBody)
//                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }
}