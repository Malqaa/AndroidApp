package com.malka.androidappp.newPhase.presentation.myOrderFragment

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import com.malka.androidappp.newPhase.domain.models.orderDetails.OrderDetailsResp
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDResp
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MyOrdersViewModel: BaseViewModel() {
    //var soldOutOrdersRespObserver: MutableLiveData<OrderListResp> = MutableLiveData()
    var currentOrderRespObserver: MutableLiveData<OrderListResp> = MutableLiveData()
    var currentOrderByMusterIdRespObserver: MutableLiveData<OrderDetailsByMasterIDResp> = MutableLiveData()
    var soldOutOrderDetailsByOrderIdRespObserver: MutableLiveData<OrderDetailsResp> = MutableLiveData()
    var cancelOrderRespObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var isNetworkFailCancel: MutableLiveData<Boolean> = MutableLiveData()
    var errorResponseCancelObserver: MutableLiveData<ErrorResponse> = MutableLiveData()

//    fun getSoldOutOrders(pageIndes: Int) {
//        if (pageIndes == 1)
//            isLoading.value = true
//        else
//            isloadingMore.value = true
//        RetrofitBuilder.GetRetrofitBuilder()
//            .getBusinessAccountOrders(pageIndes)
//            .enqueue(object : Callback<OrderListResp> {
//                override fun onFailure(call: Call<OrderListResp>, t: Throwable) {
//                    isNetworkFail.value = t !is HttpException
//                    isLoading.value = false
//                    isloadingMore.value = false
//                }
//
//                override fun onResponse(
//                    call: Call<OrderListResp>,
//                    response: Response<OrderListResp>
//                ) {
//                    isLoading.value = false
//                    isloadingMore.value = false
//                    if (response.isSuccessful) {
//                        currentOrderRespObserver.value = response.body()
//                    } else {
//                        errorResponseObserver.value = getErrorResponse(response.errorBody())
//                    }
//                }
//            })
//    }
//
    fun getSoldOutOrderDetailsByOrderId(orderId:Int){
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getOrderDetailsByOrderID(orderId)
            .enqueue(object : Callback<OrderDetailsResp> {
                override fun onFailure(call: Call<OrderDetailsResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<OrderDetailsResp>,
                    response: Response<OrderDetailsResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        soldOutOrderDetailsByOrderIdRespObserver.value = response.body()
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
    fun getFinishOOrders(pageIndes: Int,userId:String) {
        if (pageIndes == 1)
            isLoading.value = true
        else
            isloadingMore.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getFinishedOrders(pageIndes,userId)
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
    fun getCurrentOrderDetailsByMasterID(orderMasterID:Int){
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getOrderMasterDetailsByMasterOrderId(orderMasterID)
            .enqueue(object : Callback<OrderDetailsByMasterIDResp> {
                override fun onFailure(call: Call<OrderDetailsByMasterIDResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<OrderDetailsByMasterIDResp>,
                    response: Response<OrderDetailsByMasterIDResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        currentOrderByMusterIdRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun cancelOrder(orderId: Int, status: Int) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .changeOrderStatus(orderId,status)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFailCancel.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        cancelOrderRespObserver.value = response.body()
                    } else {
                        errorResponseCancelObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}