package com.malka.androidappp.newPhase.presentation.sellerDetailsActivity

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateListResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class SellerRatingViewModel : BaseViewModel(){
    var sellerRateListObservable: MutableLiveData<SellerRateListResp> = MutableLiveData()

    fun getSellerRates(page: Int,rate:Int?) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore .value = true
        getRetrofitBuilder()
            .getSellerRates( page,rate)
            .enqueue(object : Callback<SellerRateListResp> {
                override fun onFailure(call: Call<SellerRateListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value=false
                }

                override fun onResponse(
                    call: Call<SellerRateListResp>,
                    response: Response<SellerRateListResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value=false
                    if (response.isSuccessful) {
                        sellerRateListObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun getBuyerRates(page: Int,rate:Int?) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore .value = true
        getRetrofitBuilder()
            .getBuyerRates( page,rate)
            .enqueue(object : Callback<SellerRateListResp> {
                override fun onFailure(call: Call<SellerRateListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value=false
                }
                override fun onResponse(
                    call: Call<SellerRateListResp>,
                    response: Response<SellerRateListResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value=false
                    if (response.isSuccessful) {
                        sellerRateListObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
}