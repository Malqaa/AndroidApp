package com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class SellerViewModel : BaseViewModel() {
    var sellerProductsRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()

    fun getSellerListProduct(page: Int, sellerProviderID: String, businessAccountId: String) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore.value = true

        RetrofitBuilder.GetRetrofitBuilder()
            .getListSellerProducts(page,sellerProviderID,businessAccountId)
            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value = false
                }

                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value = false
                    if (response.isSuccessful) {
                        sellerProductsRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}