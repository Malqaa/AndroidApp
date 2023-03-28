package com.malka.androidappp.newPhase.presentation.addProduct.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp.HomeCategoryProductResp
import com.malka.androidappp.newPhase.domain.models.pakatResp.PakatResp
import com.malka.androidappp.newPhase.domain.models.productTags.CategoryTagsResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AddProductViewModel :BaseViewModel() {

    var getListCategoriesByProductNameObserver:MutableLiveData<CategoryTagsResp> = MutableLiveData()
    var getDynamicSpecificationObserver:MutableLiveData<DynamicSpecificationResp> = MutableLiveData()
    var getPakatRespObserver:MutableLiveData<PakatResp> = MutableLiveData()
    var categoriesObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoriesErrorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
    var isLoadingAllCategory:MutableLiveData<Boolean> =MutableLiveData()
    fun getListCategoriesByProductName(productName:String){
        isLoading.value=true
        RetrofitBuilder.GetRetrofitBuilder()
            .getListCategoriesByProductName(productName)
            .enqueue(object : Callback<CategoryTagsResp> {
                override fun onFailure(call: Call<CategoryTagsResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value=false
                }

                override fun onResponse(
                    call: Call<CategoryTagsResp>,
                    response: Response<CategoryTagsResp>
                ) {
                    isLoading.value=false
                    if (response.isSuccessful) {
                       getListCategoriesByProductNameObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun getSubCategoriesByCategoryID(categoryId:Int){

    }
    fun getDynamicSpecification(categoryId: Int){
        isLoading.value=true
        RetrofitBuilder.GetRetrofitBuilder()
            .getDynamicSpecificationForCategory(categoryId.toString())
            .enqueue(object : Callback<DynamicSpecificationResp> {
                override fun onFailure(call: Call<DynamicSpecificationResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value=false
                }

                override fun onResponse(
                    call: Call<DynamicSpecificationResp>,
                    response: Response<DynamicSpecificationResp>
                ) {
                    isLoading.value=false
                    if (response.isSuccessful) {
                        getDynamicSpecificationObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun getPakatList(categoryId: Int){
        isLoading.value=true
        RetrofitBuilder.GetRetrofitBuilder()
            .getAllPakatList(categoryId.toString())
            .enqueue(object : Callback<PakatResp> {
                override fun onFailure(call: Call<PakatResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value=false
                }

                override fun onResponse(
                    call: Call<PakatResp>,
                    response: Response<PakatResp>
                ) {
                    isLoading.value=false
                    if (response.isSuccessful) {
                        getPakatRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getAllCategories() {
        isLoadingAllCategory.value=true
        RetrofitBuilder.GetRetrofitBuilder()
            .getAllCategories()
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoadingAllCategory.value=false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoadingAllCategory.value=false
                    if (response.isSuccessful) {
                        categoriesObserver.value = response.body()
                    } else {
                        categoriesErrorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}