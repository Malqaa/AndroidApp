package com.malka.androidappp.newPhase.presentation.account_fragment.myProducts.viewModel

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MyProductViewModel : BaseViewModel() {

    var forSaleProductRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var notForSaleProductRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var soldOutOrdersRespObserver: MutableLiveData<OrderListResp> = MutableLiveData()
    var addDiscountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var loadingAddDiscountDialog:MutableLiveData<Boolean> = MutableLiveData()
    fun getForSaleProduct() {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getMyProduct()
            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        forSaleProductRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
   fun getForDidNotSaleProducts(){
       isLoading.value = true
       RetrofitBuilder.GetRetrofitBuilder()
           .getListDidntSellProducts()
           .enqueue(object : Callback<ProductListResp> {
               override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                   isNetworkFail.value = t !is HttpException
                   isLoading.value = false
               }

               override fun onResponse(
                   call: Call<ProductListResp>,
                   response: Response<ProductListResp>
               ) {
                   isLoading.value = false
                   if (response.isSuccessful) {
                       notForSaleProductRespObserver.value = response.body()
                   } else {
                       errorResponseObserver.value = getErrorResponse(response.errorBody())
                   }
               }
           })
   }
    fun getSoldOutOrders(pageIndes: Int) {
        if (pageIndes == 1)
            isLoading.value = true
        else
            isloadingMore.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getBusinessAccountOrders(pageIndes)
            .enqueue(object : Callback<OrderListResp> {
                override fun onFailure(call: Call<OrderListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value = false
                }

                override fun onResponse(
                    call: Call<OrderListResp>,
                    response: Response<OrderListResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value = false
                    if (response.isSuccessful) {
                        soldOutOrdersRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun addDiscount(productId: Int, discountPrice: Float, finaldate: String){
        loadingAddDiscountDialog.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .addDiscount(productId,discountPrice,finaldate)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    loadingAddDiscountDialog.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    loadingAddDiscountDialog.value = false
                    if (response.isSuccessful) {
                        addDiscountObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}