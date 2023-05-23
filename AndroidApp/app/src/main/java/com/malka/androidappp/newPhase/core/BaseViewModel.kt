package com.malka.androidappp.newPhase.core

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import com.malka.androidappp.newPhase.domain.models.configrationResp.ConfigurationResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

open class BaseViewModel : ViewModel() {
    val STATUS_UNAUTHORIZED = 401
    val STATUS_INVALID_TOKEN = 403
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isloadingMore: MutableLiveData<Boolean> = MutableLiveData()
    var isNetworkFail: MutableLiveData<Boolean> = MutableLiveData()
    var errorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
   /***/
    var addProductToFavObserver:MutableLiveData<GeneralResponse> = MutableLiveData()
    var isNetworkFailProductToFav: MutableLiveData<Boolean> = MutableLiveData()
    var errorResponseObserverProductToFav: MutableLiveData<ErrorResponse> = MutableLiveData()
    fun getErrorResponse(body: ResponseBody?): ErrorResponse {
        try {
            val adapter: TypeAdapter<ErrorResponse> =
                Gson().getAdapter<ErrorResponse>(ErrorResponse::class.java)
            return adapter.fromJson(body?.string())
        } catch (e: Exception) {
            return ErrorResponse()
        }
    }

    fun addProductToFav(ProductID:Int){
        isLoading.value=true
        RetrofitBuilder.GetRetrofitBuilder()
            .addProductToFav(ProductID)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFailProductToFav.value = t !is HttpException
                    isLoading.value=false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value=false
                    if (response.isSuccessful) {
                        addProductToFavObserver.value = response.body()
                    } else {
                        errorResponseObserverProductToFav.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }


    var configurationRespObserver: MutableLiveData<ConfigurationResp> = MutableLiveData()
    fun getConfigurationResp(configKey: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getConfigurationData(configKey)
            .enqueue(object : Callback<ConfigurationResp> {
                override fun onFailure(call: Call<ConfigurationResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ConfigurationResp>,
                    response: Response<ConfigurationResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        configurationRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

}