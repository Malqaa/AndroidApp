package com.malka.androidappp.newPhase.presentation.myOrderFragment

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderListResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MyOrdersViewModel: BaseViewModel() {
    //var soldOutOrdersRespObserver: MutableLiveData<OrderListResp> = MutableLiveData()
    var currentOrderRespObserver: MutableLiveData<OrderListResp> = MutableLiveData()
    fun getSoldOutOrders(pageIndes: Int) {
        if (pageIndes == 1)
            isLoading.value = true
        else
            isloadingMore.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getBusinessAccountOrders(pageIndes)
            .enqueue(object : Callback<OrderListResp> {
                override fun onFailure(call: Call<OrderListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value = false
                }

                override fun onResponse(
                    call: Call<OrderListResp>,
                    response: Response<OrderListResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value = false
                    if (response.isSuccessful) {
                        currentOrderRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun getCurrentOrderOrders(pageIndes: Int,userId:String) {
        if (pageIndes == 1)
            isLoading.value = true
        else
            isloadingMore.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getCurrentOrders(pageIndes,userId)
            .enqueue(object : Callback<OrderListResp> {
                override fun onFailure(call: Call<OrderListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value = false
                }

                override fun onResponse(
                    call: Call<OrderListResp>,
                    response: Response<OrderListResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value = false
                    if (response.isSuccessful) {
                        currentOrderRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}