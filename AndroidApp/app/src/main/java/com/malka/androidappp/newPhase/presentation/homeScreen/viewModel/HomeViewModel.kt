package com.malka.androidappp.newPhase.presentation.homeScreen.viewModel

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import com.malka.androidappp.newPhase.domain.models.homeSilderResp.HomeSliderResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp.CategoryProductItem
import com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp.HomeCategoryProductResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class HomeViewModel : BaseViewModel() {
    var sliderObserver: MutableLiveData<HomeSliderResp> = MutableLiveData()
    var searchObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoriesObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoriesErrorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
    var homeCategoryProductObserver: MutableLiveData<HomeCategoryProductResp> = MutableLiveData()
    var homeCategoryProductErrorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
    var isLoadingAllCategory:MutableLiveData<Boolean> =MutableLiveData()
    var lastViewProductsObserver:MutableLiveData<ProductListResp> =MutableLiveData()
    fun getSliderData(slideType:Int) {
        RetrofitBuilder.GetRetrofitBuilder()
            .getHomeSlidersImages(slideType)
            .enqueue(object : Callback<HomeSliderResp> {
                override fun onFailure(call: Call<HomeSliderResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<HomeSliderResp>,
                    response: Response<HomeSliderResp>
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

    fun getListHomeCategoryProduct() {
        RetrofitBuilder.GetRetrofitBuilder()
            .ListHomeCategoryProduct()
            .enqueue(object : Callback<HomeCategoryProductResp> {
                override fun onFailure(call: Call<HomeCategoryProductResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<HomeCategoryProductResp>,
                    response: Response<HomeCategoryProductResp>
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

    fun getLastViewedProduct() {
        RetrofitBuilder.GetRetrofitBuilder()
            .getListLastView()
            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                }
                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {
                    if (response.isSuccessful) {
                        lastViewProductsObserver.value = response.body()
                    }
//
                }
            })

    }
}