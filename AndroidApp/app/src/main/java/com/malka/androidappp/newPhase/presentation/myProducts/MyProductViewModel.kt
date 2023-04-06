package com.malka.androidappp.newPhase.presentation.myProducts

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MyProductViewModel : BaseViewModel() {

    var forSaleProductRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()

    fun getForSaleProduct(){
        isLoading.value=true
        RetrofitBuilder.GetRetrofitBuilder()
            .getMyProduct()
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
                        forSaleProductRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}