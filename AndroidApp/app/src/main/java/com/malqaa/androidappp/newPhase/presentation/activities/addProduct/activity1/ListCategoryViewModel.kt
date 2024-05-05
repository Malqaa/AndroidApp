package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity1

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.ErrorResponse
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.AddFollowObj
import com.malqaa.androidappp.newPhase.domain.models.categoryResp.CategoriesResp
import com.malqaa.androidappp.newPhase.domain.models.productTags.CategoryTagsResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import retrofit2.Call
import retrofit2.HttpException

class ListCategoryViewModel : BaseViewModel() {

    var getListCategoriesByProductNameObserver: MutableLiveData<CategoryTagsResp> =
        MutableLiveData()
    var categoryListObserver: MutableLiveData<CategoriesResp> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()

    private var callSubCategories: Call<CategoriesResp>? =null
    private var callListCategories: Call<CategoryTagsResp>? =null
    private var callFollow: Call<GeneralResponse>? = null
    var isFollowCategory: MutableLiveData<Boolean> = MutableLiveData()

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


    fun followCategoryAPI(categoryIDS: ArrayList<Int>, context: Activity) {
        HelpFunctions.startProgressBar(context)
        callFollow = getRetrofitBuilder().addFollow(
            AddFollowObj(categoryIDS)
        )
        callApi(callFollow!!,
            onSuccess = {
                HelpFunctions.dismissProgressBar()
                println("tttt add " + Gson().toJson(it))
                isFollowCategory.value = true
            },
            onFailure = { throwable, statusCode, errorBody ->
                HelpFunctions.dismissProgressBar()
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    val errResponse: ErrorResponse = getErrorResponse(statusCode, errorBody)!!
                    if (errResponse.message == "Categories already exists") {
                        error.value = errResponse.message
                    }else{
                        errorResponseObserver.value =
                            getErrorResponse(statusCode, errorBody)
                    }
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
        if (callFollow != null) {
            callFollow?.cancel()
        }
    }
}