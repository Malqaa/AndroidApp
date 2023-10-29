package com.malka.androidappp.newPhase.presentation.shipmentRateActivity

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.orderRateResp.RateObject
import com.malka.androidappp.newPhase.domain.models.orderRateResp.ShipmentRateResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ShipmentRateViewModel :BaseViewModel() {
    var addShipmentRateObserver:MutableLiveData<GeneralResponse> = MutableLiveData()
    var getShipmentRate:MutableLiveData<ShipmentRateResp> = MutableLiveData()
    fun addShipmentRate( rateObject: RateObject){
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .addShipmentRate(rateObject)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        addShipmentRateObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
    fun getShipmentRate(orderId:Int){
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getShipmentRate(orderId)
            .enqueue(object : Callback<ShipmentRateResp> {
                override fun onFailure(call: Call<ShipmentRateResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ShipmentRateResp>,
                    response: Response<ShipmentRateResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getShipmentRate.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
}