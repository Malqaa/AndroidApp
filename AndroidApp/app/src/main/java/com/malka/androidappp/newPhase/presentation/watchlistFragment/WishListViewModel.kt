package com.malka.androidappp.newPhase.presentation.watchlistFragment

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.callApi
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import retrofit2.Call
import retrofit2.HttpException

class WishListViewModel:BaseViewModel() {

    var wishListRespObserver:MutableLiveData<ProductListResp> = MutableLiveData()
    private var callUserWatchlist: Call<ProductListResp>? = null

    fun getWishListProduct(){
        isLoading.value=true

        callUserWatchlist = getRetrofitBuilder()
            .getUserWatchlist()
        callApi(callUserWatchlist!!,
            onSuccess = {
                isLoading.value = false
                wishListRespObserver.value = it
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
        if (callUserWatchlist != null) {
            callUserWatchlist?.cancel()
        }
    }
}