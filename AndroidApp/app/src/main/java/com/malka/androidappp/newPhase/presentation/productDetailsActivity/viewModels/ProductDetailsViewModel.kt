package com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductResp
import com.malka.androidappp.newPhase.domain.models.questionResp.AddQuestionResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ProductDetailsViewModel: BaseViewModel() {

    var productDetailsObservable :MutableLiveData<ProductResp> = MutableLiveData()
    var addQuestionObservable :MutableLiveData<AddQuestionResp> = MutableLiveData()
    var getSimilarProductObservable :MutableLiveData<ProductListResp> = MutableLiveData()

    fun getProductDetailsById(productId:Int){
        isLoading.value=true
        RetrofitBuilder.GetRetrofitBuilder()
            .getProductDetailById2(productId)
            .enqueue(object : Callback<ProductResp> {
                override fun onFailure(call: Call<ProductResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value=false
                }

                override fun onResponse(
                    call: Call<ProductResp>,
                    response: Response<ProductResp>
                ) {
                    isLoading.value=false
                    if (response.isSuccessful) {
                        productDetailsObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun addQuestion(productId:Int,question:String){
        val data:HashMap<String,Any> = HashMap()
        data["Question"]=question
        data["ProductId"]=productId
        data["lang"]=ConstantObjects.currentLanguage
        isLoading.value=true
        RetrofitBuilder.GetRetrofitBuilder()
         .addQuestion(question.requestBody(),productId.toString().requestBody(),ConstantObjects.currentLanguage.requestBody())
            //?.addQuestion(data)
            .enqueue(object : Callback<AddQuestionResp> {
                override fun onFailure(call: Call<AddQuestionResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value=false
                }

                override fun onResponse(
                    call: Call<AddQuestionResp>,
                    response: Response<AddQuestionResp>
                ) {
                    isLoading.value=false
                    println("hhhh "+response.code())
                    if (response.isSuccessful) {
                        addQuestionObservable.value = response.body()
                    } else {
                        println("hhhh "+Gson().toJson(getErrorResponse(response.errorBody())))
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun getSimilarProduct(productId:Int,page:Int){
        RetrofitBuilder.GetRetrofitBuilder()
            .getSimilarProductForOtherProduct(page,productId)

            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {

                    if (response.isSuccessful) {
                        getSimilarProductObservable.value = response.body()
                    }
                }
            })
    }
}