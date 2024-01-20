package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity1

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.categoryResp.CategoriesResp
import com.malqaa.androidappp.newPhase.domain.models.productTags.CategoryTagsResp
import retrofit2.Call
import retrofit2.HttpException

class ListCategoryViewModel : BaseViewModel() {

    var getListCategoriesByProductNameObserver: MutableLiveData<CategoryTagsResp> =
        MutableLiveData()
    var categoryListObserver: MutableLiveData<CategoriesResp> = MutableLiveData()

    private var callSubCategories: Call<CategoriesResp>? =null
    private var callListCategories: Call<CategoryTagsResp>? =null

    fun getListCategoriesByProductName(productName: String) {
        isLoading.value = true
        callListCategories = getRetrofitBuilder().getListCategoriesByProductName(productName)
        callApi(callListCategories!!,
            onSuccess = {
                isLoading.value = false
                getListCategoriesByProductNameObserver.value = it
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

    fun getSubCategoriesByCategoryID(categoryId: Int) {
        isLoading.value = true
        callSubCategories = getRetrofitBuilder().getSubCategoryByMainCategory2(categoryId.toString())
        callApi(callSubCategories!!,
            onSuccess = {
                isLoading.value = false
                categoryListObserver.value = it
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
        if (callListCategories != null) {
            callListCategories?.cancel()
        }
        if (callSubCategories != null) {
            callSubCategories?.cancel()
        }
    }
}