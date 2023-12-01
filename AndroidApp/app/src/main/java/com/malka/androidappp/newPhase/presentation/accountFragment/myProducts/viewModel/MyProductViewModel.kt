package com.malka.androidappp.newPhase.presentation.accountFragment.myProducts.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.configrationResp.ConfigurationData
import com.malka.androidappp.newPhase.domain.models.configrationResp.ConfigurationResp
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import kotlinx.android.synthetic.main.dialog_acceot_offer.btnSend
import kotlinx.android.synthetic.main.dialog_acceot_offer.close_alert_bid
import kotlinx.android.synthetic.main.dialog_acceot_offer.progressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MyProductViewModel : BaseViewModel() {

    var forSaleProductRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var notForSaleProductRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var soldOutOrdersRespObserver: MutableLiveData<OrderListResp> = MutableLiveData()
    var addDiscountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var removeProductObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var repostProductObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var configurationDataObserver:MutableLiveData<ConfigurationData> =MutableLiveData()
    var loadingAddDiscountDialog: MutableLiveData<Boolean> = MutableLiveData()
    fun getForSaleProduct() {
        isLoading.value = true
        getRetrofitBuilder()
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getForDidNotSaleProducts() {
        isLoading.value = true
        getRetrofitBuilder()
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getSoldOutOrders(pageIndes: Int) {
        if (pageIndes == 1)
            isLoading.value = true
        else
            isloadingMore.value = true
        getRetrofitBuilder()
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun addDiscount(productId: Int, discountPrice: Float, finaldate: String) {
        loadingAddDiscountDialog.value = true
        getRetrofitBuilder()
            .addDiscount(productId, discountPrice, finaldate)
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun removeProduct(productId: Int) {
        loadingAddDiscountDialog.value = true
        getRetrofitBuilder()
            .removeProduct(productId)
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
                        removeProductObserver.value = response.body()

                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun repostProduct(productId: Int) {
        loadingAddDiscountDialog.value = true
        getRetrofitBuilder()
            .repostProduct(productId)
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
                        repostProductObserver.value = response.body()

                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getExpireHours() {
        getRetrofitBuilder().getConfigurationData(
            ConstantObjects.configration_OfferExpiredHours
        ).enqueue(object : Callback<ConfigurationResp> {
            override fun onFailure(call: Call<ConfigurationResp>, t: Throwable) {
                isNetworkFail.value = t !is HttpException
                loadingAddDiscountDialog.value = false
            }

            override fun onResponse(
                call: Call<ConfigurationResp>,
                response: Response<ConfigurationResp>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.status_code == 200) {
                            configurationDataObserver.value=it?.configurationData
                        }
                    }
                } else {
                    errorResponseObserver.value =
                        getErrorResponse(response.code(), response.errorBody())
                }

            }
        })
    }
}