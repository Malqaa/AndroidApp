package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonSyntaxException
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.negotiationOfferResp.NegotiationOfferResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.HttpException

class NegotiationOffersViewModel:BaseViewModel() {
    var purchaseProductsOffersObserver:MutableLiveData<NegotiationOfferResp> = MutableLiveData()
    var noOffersObserver:MutableLiveData<Boolean> = MutableLiveData()
    var loadingDialogObserver:MutableLiveData<Boolean> = MutableLiveData()
    var purchaseOfferObserver:MutableLiveData<GeneralResponse> = MutableLiveData()
    var cancelOfferObserver:MutableLiveData<GeneralResponse> = MutableLiveData()


    private var callPurchaseProductsOffers: Call<NegotiationOfferResp>? = null
    private var callSaleProductsOffers: Call<NegotiationOfferResp>? = null
    private var callCancelOfferProvider: Call<GeneralResponse>? = null
    private var callCancelOffer: Call<GeneralResponse>? = null
    private var callPurchaseOffer: Call<GeneralResponse>? = null

    fun closeAllCall() {
        if (callPurchaseProductsOffers != null) {
            callPurchaseProductsOffers?.cancel()
        }
        if (callSaleProductsOffers != null) {
            callSaleProductsOffers?.cancel()
        }
        if (callCancelOfferProvider != null) {
            callCancelOfferProvider?.cancel()
        }
        if (callCancelOffer != null) {
            callCancelOffer?.cancel()
        }
        if (callPurchaseOffer != null) {
            callPurchaseOffer?.cancel()
        }
    }

    fun getPurchaseProductsOffers(isSent:Boolean){
        isLoading.value = true

        callPurchaseProductsOffers = getRetrofitBuilder().getPurchaseProductsOffers(isSent)
        callApi(callPurchaseProductsOffers!!,
            onSuccess = {
                isLoading.value = false
                purchaseProductsOffersObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null){
                    if(throwable is JsonSyntaxException){
                        noOffersObserver.value=true
                    }else {
                        isNetworkFail.value = throwable !is HttpException
                    }
                }
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
    fun getSaleProductsOffers(isSent:Boolean){
        isLoading.value = true
        callSaleProductsOffers = getRetrofitBuilder().getSaleProductsOffers(isSent)
        callApi(callSaleProductsOffers!!,
            onSuccess = {
                isLoading.value = false
                purchaseProductsOffersObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null){
                    if(throwable is JsonSyntaxException){
                        noOffersObserver.value=true
                    }else {
                        isNetworkFail.value = throwable !is HttpException
                    }
                }
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

    fun cancelOfferProvider(offerId:Int){
        loadingDialogObserver.value = true
        callCancelOfferProvider = getRetrofitBuilder().cancelProductOfferByProvider(offerId)
        callApi(callCancelOfferProvider!!,
            onSuccess = {
                loadingDialogObserver.value = false
                cancelOfferObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                loadingDialogObserver.value = false
                if (throwable != null && errorBody == null){
                        isNetworkFail.value = throwable !is HttpException
                }
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                loadingDialogObserver.value = false
                needToLogin.value = true
            })
    }

    fun cancelOffer(offerId:Int){
        loadingDialogObserver.value = true

        callCancelOffer = getRetrofitBuilder().cancelProductOfferByClient(offerId)
        callApi(callCancelOffer!!,
            onSuccess = {
                loadingDialogObserver.value = false
                cancelOfferObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                loadingDialogObserver.value = false
                if (throwable != null && errorBody == null){
                    isNetworkFail.value = throwable !is HttpException
                }
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                loadingDialogObserver.value = false
                needToLogin.value = true
            })

    }

    fun purchaseOffer(offerId:Int){
        loadingDialogObserver.value = true
        callPurchaseOffer= getRetrofitBuilder().purchaseProductByOffer(offerId)
        callApi(callPurchaseOffer!!,
            onSuccess = {
                loadingDialogObserver.value = false
                purchaseOfferObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                loadingDialogObserver.value = false
                if (throwable != null && errorBody == null){
                    isNetworkFail.value = throwable !is HttpException
                }
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                loadingDialogObserver.value = false
                needToLogin.value = true
            })
    }
}