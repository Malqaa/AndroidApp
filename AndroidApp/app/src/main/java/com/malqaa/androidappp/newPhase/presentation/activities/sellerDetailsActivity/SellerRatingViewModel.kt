package com.malqaa.androidappp.newPhase.presentation.activities.sellerDetailsActivity

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateListResp
import retrofit2.Call
import retrofit2.HttpException

class SellerRatingViewModel : BaseViewModel() {
    var sellerRateListObservable: MutableLiveData<SellerRateListResp> = MutableLiveData()

    private var callSellerRates: Call<SellerRateListResp>? = null
    private var callBuyerRates: Call<SellerRateListResp>? = null

    fun getSellerRates(page: Int, rate: Int?) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore.value = true

        callSellerRates = getRetrofitBuilder().getSellerRates(page, rate)
        callApi(callSellerRates!!,
            onSuccess = {
                isLoading.value = false
                isloadingMore.value = false
                sellerRateListObservable.value = it
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

    fun getBuyerRates(page: Int, rate: Int?) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore.value = true
        callBuyerRates = getRetrofitBuilder().getBuyerRates(page, rate)
        callApi(callBuyerRates!!,
            onSuccess = {
                isLoading.value = false
                isloadingMore.value = false
                sellerRateListObservable.value = it
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

    fun closeAllCall() {
        if (callSellerRates != null) {
            callSellerRates?.cancel()
        }
        if (callBuyerRates != null) {
            callBuyerRates?.cancel()
        }

    }
}