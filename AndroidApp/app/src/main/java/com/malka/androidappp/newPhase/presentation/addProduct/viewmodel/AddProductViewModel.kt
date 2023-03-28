package com.malka.androidappp.newPhase.presentation.addProduct.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malka.androidappp.newPhase.domain.models.productTags.CategoryTagsResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AddProductViewModel :BaseViewModel() {

    var getListCategoriesByProductNameObserver:MutableLiveData<CategoryTagsResp> = MutableLiveData()
   var getDynamicSpecificationObserver:MutableLiveData<DynamicSpecificationResp> = MutableLiveData()
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
}