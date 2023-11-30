package com.malka.androidappp.newPhase.presentation.addProduct.activity7

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShippingViewModel : BaseViewModel() {
    var shippingListObserver: MutableLiveData<ShippingOptionResp> =
        MutableLiveData<ShippingOptionResp>()


    fun getAllShippingOptions() {
        //isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getAllShippingOptions()
            .enqueue(object : Callback<ShippingOptionResp> {
                override fun onFailure(call: Call<ShippingOptionResp>, t: Throwable) {
//                    isNetworkFail.value = t !is HttpException
//                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ShippingOptionResp>,
                    response: Response<ShippingOptionResp>
                ) {
                    // isLoading.value = false
                    if (response.isSuccessful) {
                        shippingListObserver.value = response.body()
                    }
//                    else {
//                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
//                    }
                }
            })
    }
}