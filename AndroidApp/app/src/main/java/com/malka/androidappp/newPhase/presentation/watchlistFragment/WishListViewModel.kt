package com.malka.androidappp.newPhase.presentation.watchlistFragment

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class WishListViewModel:BaseViewModel() {

    var wishListRespObserver:MutableLiveData<ProductListResp> = MutableLiveData()

    fun getWishListProduct(){
        isLoading.value=true
        RetrofitBuilder.GetRetrofitBuilder()
            .getUserWatchlist()
            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value=false
                }

                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {
                    isLoading.value=false
                    if (response.isSuccessful) {
                        wishListRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
}