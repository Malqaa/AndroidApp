package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.viewModel

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.configrationResp.ConfigurationData
import com.malqaa.androidappp.newPhase.domain.models.configrationResp.ConfigurationResp
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderListResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.HttpException

class MyProductViewModel : BaseViewModel() {

    var forSaleProductRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var notForSaleProductRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var soldOutOrdersRespObserver: MutableLiveData<OrderListResp> = MutableLiveData()
    var addDiscountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var removeProductObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var repostProductObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var configurationDataObserver: MutableLiveData<ConfigurationData> = MutableLiveData()
    var loadingAddDiscountDialog: MutableLiveData<Boolean> = MutableLiveData()

    private var callForSaleProduct: Call<ProductListResp>? = null
    private var callSoldOutOrders: Call<OrderListResp>? = null
    private var callForDidNotSaleProducts: Call<ProductListResp>? = null
    private var callRemoveProduct: Call<GeneralResponse>? = null
    private var callRepostProduct: Call<GeneralResponse>? = null
    private var callAddDiscount: Call<GeneralResponse>? = null
    private var callExpireHours: Call<ConfigurationResp>? = null


    fun closeAllCall() {
        if (callForSaleProduct != null) {
            callForSaleProduct?.cancel()
        }
        if (callSoldOutOrders != null) {
            callSoldOutOrders?.cancel()
        }
        if (callForDidNotSaleProducts != null) {
            callForDidNotSaleProducts?.cancel()
        }
        if (callRemoveProduct != null) {
            callRemoveProduct?.cancel()
        }
        if (callRepostProduct != null) {
            callRepostProduct?.cancel()
        }
        if (callAddDiscount != null) {
            callAddDiscount?.cancel()
        }
        if (callExpireHours != null) {
            callExpireHours?.cancel()
        }
    }
    fun getForSaleProduct() {
        isLoading.value = true
        callForSaleProduct = getRetrofitBuilder().getMyProduct()
        callApi(callForSaleProduct!!,
            onSuccess = {
                isLoading.value = false
                forSaleProductRespObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
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

    fun getForDidNotSaleProducts() {
        isLoading.value = true

        callForDidNotSaleProducts = getRetrofitBuilder().getListDidntSellProducts()
        callApi(callForDidNotSaleProducts!!,
            onSuccess = {
                isLoading.value = false
                notForSaleProductRespObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
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

    fun getSoldOutOrders(pageIndex: Int) {
        if (pageIndex == 1)
            isLoading.value = true
        else
            isloadingMore.value = true

        callSoldOutOrders = getRetrofitBuilder().getBusinessAccountOrders(pageIndex)
        callApi(callSoldOutOrders!!,
            onSuccess = {
                isLoading.value = false
                isloadingMore.value = false
                soldOutOrdersRespObserver.value = it
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
                isLoading.value = false
                isloadingMore.value = false
                needToLogin.value = true
            })

    }

    fun addDiscount(productId: Int, discountPrice: Float, finalDate: String) {
        loadingAddDiscountDialog.value = true
        callAddDiscount = getRetrofitBuilder().addDiscount(productId, discountPrice, finalDate)
        callApi(callAddDiscount!!,
            onSuccess = {
                loadingAddDiscountDialog.value = false
                addDiscountObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                loadingAddDiscountDialog.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                loadingAddDiscountDialog.value = false
                needToLogin.value = true
            })
    }

    fun removeProduct(productId: Int) {
        loadingAddDiscountDialog.value = true
        callRemoveProduct = getRetrofitBuilder().removeProduct(productId)
        callApi(callRemoveProduct!!,
            onSuccess = {
                loadingAddDiscountDialog.value = false
                removeProductObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                loadingAddDiscountDialog.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                loadingAddDiscountDialog.value = false
                needToLogin.value = true
            })
    }

    fun repostProduct(productId: Int) {
        loadingAddDiscountDialog.value = true
        callRepostProduct = getRetrofitBuilder()
            .repostProduct(productId)

        callApi(callRepostProduct!!,
            onSuccess = {
                loadingAddDiscountDialog.value = false
                repostProductObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                loadingAddDiscountDialog.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                loadingAddDiscountDialog.value = false
                needToLogin.value = true
            })
    }

    fun getExpireHours() {
        callExpireHours = getRetrofitBuilder().getConfigurationData(ConstantObjects.configration_OfferExpiredHours)

        callApi(callExpireHours!!,
            onSuccess = {
                loadingAddDiscountDialog.value = false
                if (it.status_code == 200) {
                    configurationDataObserver.value=it.configurationData
                }
            },
            onFailure = { throwable, statusCode, errorBody ->
                loadingAddDiscountDialog.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                loadingAddDiscountDialog.value = false
                needToLogin.value = true
            })
                
                
    }
}