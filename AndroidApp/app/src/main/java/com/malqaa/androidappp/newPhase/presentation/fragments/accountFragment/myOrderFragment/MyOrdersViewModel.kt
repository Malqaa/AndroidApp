package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.model.ExtendBankTransferPaymentPeriodRequest
import com.malqaa.androidappp.newPhase.data.network.model.SellerConfirmOrRejectBankTransferPaymentRequest
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.ErrorResponse
import com.malqaa.androidappp.newPhase.domain.models.orderDetails.OrderDetailsResp
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDResp
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderListResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.HttpException

class MyOrdersViewModel : BaseViewModel() {
    var currentOrderRespObserver: MutableLiveData<OrderListResp> = MutableLiveData()
    var currentOrderByMusterIdRespObserver: MutableLiveData<OrderDetailsByMasterIDResp> =
        MutableLiveData()
    var soldOutOrderDetailsByOrderIdRespObserver: MutableLiveData<OrderDetailsResp> =
        MutableLiveData()
    var changeOrderRespObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var isNetworkFailCancel: MutableLiveData<Boolean> = MutableLiveData()
    var errorResponseCancelObserver: MutableLiveData<ErrorResponse> = MutableLiveData()

    private var callSoldOutOrder: Call<OrderDetailsResp>? = null
    private var callCurrentOrders: Call<OrderListResp>? = null
    private var callFinishOOrders: Call<OrderListResp>? = null
    private var callCurrentOrderDetails: Call<OrderDetailsByMasterIDResp>? = null
    private var callChangeOrderStatus: Call<GeneralResponse>? = null

    fun closeAllCall() {
        if (callSoldOutOrder != null) {
            callSoldOutOrder?.cancel()
        }
        if (callCurrentOrders != null) {
            callCurrentOrders?.cancel()
        }
        if (callFinishOOrders != null) {
            callFinishOOrders?.cancel()
        }
        if (callCurrentOrderDetails != null) {
            callCurrentOrderDetails?.cancel()
        }
        if (callChangeOrderStatus != null) {
            callChangeOrderStatus?.cancel()
        }
    }

    fun getSoldOutOrderDetailsByOrderId(orderId: Int) {
        isLoading.value = true
        callSoldOutOrder = getRetrofitBuilder().getOrderDetailsByOrderID(orderId)
        callApi(
            callSoldOutOrder!!,
            onSuccess = {
                isLoading.value = false
                soldOutOrderDetailsByOrderIdRespObserver.value = it
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

    fun getCurrentOrderOrders(pageIndex: Int, userId: String) {
        if (pageIndex == 1)
            isLoading.value = true
        else
            isloadingMore.value = true

        callCurrentOrders = getRetrofitBuilder().getCurrentOrders(pageIndex, userId)
        callApi(
            callCurrentOrders!!,
            onSuccess = {
                isloadingMore.value = false
                isLoading.value = false
                currentOrderRespObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                isloadingMore.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isloadingMore.value = false
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun getFinishOOrders(pageIndex: Int, userId: String) {
        if (pageIndex == 1)
            isLoading.value = true
        else
            isloadingMore.value = true
        callFinishOOrders = getRetrofitBuilder().getFinishedOrders(pageIndex, userId)
        callApi(
            callFinishOOrders!!,
            onSuccess = {
                isloadingMore.value = false
                isLoading.value = false
                currentOrderRespObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                isloadingMore.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isloadingMore.value = false
                isLoading.value = false
                needToLogin.value = true
            })

    }

    fun getCurrentOrderDetailsByMasterID(orderMasterID: Int) {
        isLoading.value = true
        callCurrentOrderDetails =
            getRetrofitBuilder().getOrderMasterDetailsByMasterOrderId(orderMasterID)
        callApi(
            callCurrentOrderDetails!!,
            onSuccess = {
                isLoading.value = false
                currentOrderByMusterIdRespObserver.value = it
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

    fun changeOrderStatus(
        orderId: Int,
        orderStatus: Int,
        confirmationCode: Int? = null
    ) {
        isLoading.value = true
        callChangeOrderStatus = getRetrofitBuilder().changeOrderStatus(
            orderId = orderId,
            orderStatus = orderStatus,
            confirmationCode = confirmationCode
        )
        callApi(
            callChangeOrderStatus!!,
            onSuccess = {
                isLoading.value = false
                changeOrderRespObserver.value = it
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

    fun sellerConfirmOrRejectBankTransferPayment(
        request: SellerConfirmOrRejectBankTransferPaymentRequest
    ) {
        isLoading.value = true
        callChangeOrderStatus =
            getRetrofitBuilder().sellerConfirmOrRejectBankTransferPayment(request = request)
        callApi(
            callChangeOrderStatus!!,
            onSuccess = {
                isLoading.value = false
                // TODO 01: change this code
                changeOrderRespObserver.value = it
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

    fun extendBankTransferPaymentPeriod(
        request: ExtendBankTransferPaymentPeriodRequest
    ) {
        isLoading.value = true
        callChangeOrderStatus =
            getRetrofitBuilder().extendBankTransferPaymentPeriod(request = request)
        callApi(
            callChangeOrderStatus!!,
            onSuccess = {
                isLoading.value = false
                // TODO 02: change this code
                changeOrderRespObserver.value = it
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
}