package com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity.viewModel

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.HttpException

class SellerViewModel : BaseViewModel() {
    var sellerProductsRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var addSellerToFavObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var removeSellerToFavObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var sellerLoading: MutableLiveData<Boolean> = MutableLiveData()

    private var callSellerListProduct: Call<ProductListResp>? = null
    private var callAddSellerToFav: Call<GeneralResponse>? = null
    private var callRemoveSellerToFav: Call<GeneralResponse>? = null

    fun getSellerListProduct(page: Int, sellerProviderID: String, businessAccountId: String) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore.value = true

        getRetrofitBuilder()
            .getListSellerProducts(page, sellerProviderID, businessAccountId)
        callSellerListProduct =
            getRetrofitBuilder().getListSellerProducts(page, sellerProviderID, businessAccountId)
        callApi(callSellerListProduct!!,
            onSuccess = {
                isLoading.value = false
                isloadingMore.value = false
                sellerProductsRespObserver.value = it
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
                sellerLoading.value = false
                needToLogin.value = true
            })

    }

    fun addSellerToFav(sellerProviderID: String?, businessAccountId: String?) {
        sellerLoading.value = true
        getRetrofitBuilder()
            .addFavoriteSeller(sellerProviderID, businessAccountId)

        callAddSellerToFav =
            getRetrofitBuilder().addFavoriteSeller(sellerProviderID, businessAccountId)
        callApi(callAddSellerToFav!!,
            onSuccess = {
                sellerLoading.value = false
                addSellerToFavObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                sellerLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                sellerLoading.value = false
                needToLogin.value = true
            })

    }

    fun removeSellerToFav(sellerProviderID: String?, businessAccountId: String?) {
        sellerLoading.value = true

        callRemoveSellerToFav =
            getRetrofitBuilder().removeFavoriteSeller(sellerProviderID, businessAccountId)
        callApi(callRemoveSellerToFav!!,
            onSuccess = {
                sellerLoading.value = false
                removeSellerToFavObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                sellerLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                sellerLoading.value = false
                needToLogin.value = true
            })
    }


    fun closeAllCall() {
        if (callSellerListProduct != null) {
            callSellerListProduct?.cancel()
        }
        if (callAddSellerToFav != null) {
            callAddSellerToFav?.cancel()
        }
        if (callRemoveSellerToFav != null) {
            callRemoveSellerToFav?.cancel()
        }
    }
}