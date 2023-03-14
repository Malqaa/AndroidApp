package com.malka.androidappp.newPhase.presentation.homeScreen.viewModel

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class HomeViewModel : BaseViewModel() {
    var sliderObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var searchObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoriesObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoriesErrorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
    var homeCategoryProductObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var homeCategoryProductErrorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
    fun getSliderData() {
        RetrofitBuilder.GetRetrofitBuilder()
            .SliderAPI()
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful) {
                        sliderObserver.value = response.body()
                    }
//                    else{
//                        errorResponseObserver.value = getErrorResponse(response.errorBody())
//                    }
                }
            })


    }

    fun doSearch(filter: Map<String, String>) {
        RetrofitBuilder.GetRetrofitBuilder()
            .Serach(filter)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                   // isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful) {
                        searchObserver.value = response.body()
                    }
                }
            })

    }

    fun getAllCategories() {
        RetrofitBuilder.GetRetrofitBuilder()
            .getAllCategories()
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful) {
                        categoriesObserver.value = response.body()
                    } else {
                        categoriesErrorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getListHomeCategoryProduct() {
        RetrofitBuilder.GetRetrofitBuilder()
            .ListHomeCategoryProduct()
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful) {
                        homeCategoryProductObserver.value = response.body()
                    } else {
                        homeCategoryProductErrorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}