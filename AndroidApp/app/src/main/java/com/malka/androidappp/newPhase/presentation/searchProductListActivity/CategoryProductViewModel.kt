package com.malka.androidappp.newPhase.presentation.searchProductListActivity

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.callApi
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.CategoryFollowResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListSearchResp
import retrofit2.Call
import retrofit2.HttpException

class CategoryProductViewModel : BaseViewModel() {
    var productListRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var searchProductListRespObserver: MutableLiveData<ProductListSearchResp> = MutableLiveData()
    var categoryFollowRespObserver: MutableLiveData<CategoryFollowResp> = MutableLiveData()

    private var callSearchForProduct: Call<ProductListSearchResp>? = null
    private var callListCategoryFollow: Call<CategoryFollowResp>? = null
    private var callMyBids: Call<ProductListResp>? = null


    fun searchForProduct(
        categoryId: Int,
        currentLanguage: String,
        page: Int,
        countryList: List<Int>?,
        regionList: List<Int>?,
        neighoodList: List<Int>?,
        subCategoryList: List<Int>?,
        specificationList: List<String>?,
        startPrice: Float?,
        endPrice: Float?,
        productName: String?,
        comeFrom: Int
    ) {
        if (page == 1) {
            isLoading.value = true
        } else {
            isloadingMore.value = true
        }

        var stringUrl =
            "AdvancedFilter?lang=${currentLanguage}&PageRowsCount=10&pageIndex=${page}&Screen=$comeFrom"
        if (categoryId != 0) {
            stringUrl += "&mainCatId=${categoryId}"
        }
        if (productName != null) {
            stringUrl += "&productName=${productName} "
        }
        subCategoryList?.forEach { item ->
            stringUrl += "&subCatIds=${item} "
        }
        countryList?.forEach { item ->
            stringUrl += "&Countries=${item}"
        }
        regionList?.forEach { item ->
            stringUrl += "&Regions=${item}"
        }
        neighoodList?.forEach { item ->
            stringUrl += "&Neighborhoods=${item}"
        }
        specificationList?.forEach { item ->
            if (item != null)
                stringUrl += "&sepNames=${item}"
        }
        if (startPrice != 0f) {
            stringUrl += "&priceFrom=${startPrice}"
        }
        if (endPrice != 0f) {
            stringUrl += "&priceTo=${endPrice}"
        }

        callSearchForProduct = getRetrofitBuilder()
            .searchForProductInCategory(stringUrl)
        callApi(callSearchForProduct!!,
            onSuccess = {
                isloadingMore.value = false
                isLoading.value = false
                searchProductListRespObserver.value = it
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
                isloadingMore.value = false
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun getCategoryFollow() {
        callListCategoryFollow = getRetrofitBuilder().getListCategoryFollow()
        callApi(callListCategoryFollow!!,
            onSuccess = {
                isloadingMore.value = false
                isLoading.value = false
                categoryFollowRespObserver.value = it
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
                needToLogin.value = true
            })
    }

    fun getMyBids() {
        isLoading.value = true
        callMyBids = getRetrofitBuilder().getMyBids()

        callApi(callMyBids!!,
            onSuccess = {
                isLoading.value = false
                productListRespObserver.value = it
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


    fun closeAllCall() {
        if (callSearchForProduct != null) {
            callSearchForProduct?.cancel()
        }
        if (callListCategoryFollow != null) {
            callListCategoryFollow?.cancel()
        }
        if (callMyBids != null) {
            callMyBids?.cancel()
        }
    }
}