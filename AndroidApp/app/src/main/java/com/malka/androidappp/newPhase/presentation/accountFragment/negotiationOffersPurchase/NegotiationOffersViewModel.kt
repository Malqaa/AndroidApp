package com.malka.androidappp.newPhase.presentation.accountFragment.negotiationOffersPurchase

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonSyntaxException
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.negotiationOfferResp.NegotiationOfferResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class NegotiationOffersViewModel:BaseViewModel() {
    var purchaseProductsOffersObserver:MutableLiveData<NegotiationOfferResp> = MutableLiveData()
    var noOffersObserver:MutableLiveData<Boolean> = MutableLiveData()
    var loadingDialogObserver:MutableLiveData<Boolean> = MutableLiveData()
    var purchaseOfferObserver:MutableLiveData<GeneralResponse> = MutableLiveData()
    var cancelOfferObserver:MutableLiveData<GeneralResponse> = MutableLiveData()
    fun getPurchaseProductsOffers(isSent:Boolean){
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getPurchaseProductsOffers(isSent)
            .enqueue(object : Callback<NegotiationOfferResp> {
                override fun onFailure(call: Call<NegotiationOfferResp>, t: Throwable) {
                    if(t is JsonSyntaxException){
                        noOffersObserver.value=true
                        isLoading.value = false
                    }else {
                        isNetworkFail.value = t !is HttpException
                        isLoading.value = false
                    }
                }

                override fun onResponse(
                    call: Call<NegotiationOfferResp>,
                    response: Response<NegotiationOfferResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        purchaseProductsOffersObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
    fun getSaleProductsOffers(isSent:Boolean){
        isLoading.value = true

        RetrofitBuilder.GetRetrofitBuilder()
            .getSaleProductsOffers(isSent)
            .enqueue(object : Callback<NegotiationOfferResp> {
                override fun onFailure(call: Call<NegotiationOfferResp>, t: Throwable) {
                    if(t is JsonSyntaxException){
                        noOffersObserver.value=true
                        isLoading.value = false
                    }else {
                        isNetworkFail.value = t !is HttpException
                        isLoading.value = false
                    }
                }

                override fun onResponse(
                    call: Call<NegotiationOfferResp>,
                    response: Response<NegotiationOfferResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        purchaseProductsOffersObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun cancelOfferProvider(offerId:Int){
        loadingDialogObserver.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .cancelProductOfferByProvider(offerId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    loadingDialogObserver.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    loadingDialogObserver.value = false
                    if (response.isSuccessful) {
                        cancelOfferObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun cancelOffer(offerId:Int){
        loadingDialogObserver.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .cancelProductOfferByClient(offerId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    loadingDialogObserver.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    loadingDialogObserver.value = false
                    if (response.isSuccessful) {
                        cancelOfferObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }


    fun purchaseOffer(offerId:Int){
        loadingDialogObserver.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .purchaseProductByOffer(offerId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    loadingDialogObserver.value = false
                }
                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    loadingDialogObserver.value = false
                    if (response.isSuccessful) {
                        purchaseOfferObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
}