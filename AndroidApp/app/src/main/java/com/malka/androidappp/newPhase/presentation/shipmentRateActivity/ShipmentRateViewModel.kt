package com.malka.androidappp.newPhase.presentation.shipmentRateActivity

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.callApi
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.orderRateResp.RateObject
import com.malka.androidappp.newPhase.domain.models.orderRateResp.ShipmentRateResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.HttpException

class ShipmentRateViewModel :BaseViewModel() {
    var addShipmentRateObserver:MutableLiveData<GeneralResponse> = MutableLiveData()
    var getShipmentRate:MutableLiveData<ShipmentRateResp> = MutableLiveData()

    private var callAddShipmentRate: Call<GeneralResponse>? = null
    private var callShipmentRate: Call<ShipmentRateResp>? = null

    fun addShipmentRate( rateObject: RateObject){
        isLoading.value = true
        callAddShipmentRate= getRetrofitBuilder().addShipmentRate(rateObject)
        callApi(callAddShipmentRate!!,
            onSuccess = {
                isLoading.value = false
                addShipmentRateObserver.value = it
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
    fun getShipmentRate(orderId:Int){
        isLoading.value = true
        callShipmentRate= getRetrofitBuilder().getShipmentRate(orderId)
        callApi(callShipmentRate!!,
            onSuccess = {
                isLoading.value = false
                getShipmentRate.value = it
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
        if (callAddShipmentRate != null) {
            callAddShipmentRate?.cancel()
        }
        if (callShipmentRate != null) {
            callShipmentRate?.cancel()
        }
    }
}