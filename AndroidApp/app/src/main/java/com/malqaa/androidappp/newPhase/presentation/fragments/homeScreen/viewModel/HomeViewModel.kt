package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.viewModel

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.ErrorResponse
import com.malqaa.androidappp.newPhase.domain.models.NotificationResp
import com.malqaa.androidappp.newPhase.domain.models.NotificationUnReadResp
import com.malqaa.androidappp.newPhase.domain.models.homeSilderResp.HomeSliderResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.domain.models.homeCategoryProductResp.HomeCategoryProductResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.AddFavResponse
import retrofit2.Call
import retrofit2.HttpException

class HomeViewModel : BaseViewModel() {
    var sliderObserver: MutableLiveData<HomeSliderResp> = MutableLiveData()
    var saveSearchObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var searchObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoriesObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoriesErrorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
    var homeCategoryProductObserver: MutableLiveData<HomeCategoryProductResp> = MutableLiveData()
    var homeCategoryProductErrorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
    var isLoadingAllCategory: MutableLiveData<Boolean> = MutableLiveData()
    var lastViewProductsObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var languageObserver: MutableLiveData<AddFavResponse> = MutableLiveData()

    private var callSlider: Call<HomeSliderResp>? = null
    private var callSearch: Call<GeneralResponse>? = null
    private var callSaveSearch: Call<GeneralResponse>? = null
    private var callAllCategories: Call<GeneralResponse>? = null
    private var callListHomeCategoryProduct : Call<HomeCategoryProductResp>? = null
    private var callLastViewedProduct: Call<ProductListResp>? = null
    private var changeLanguage: Call<AddFavResponse>? = null

    var unreadObserve: MutableLiveData<NotificationUnReadResp> = MutableLiveData()
    private var callNotifyUnread: Call<NotificationUnReadResp>? = null

    fun getUnReadNotification(pageIndex:Int,rowCount:Int){
        isLoading.value=true

        callNotifyUnread = getRetrofitBuilder()
            .unreadNotificationsCount(pageIndex,rowCount)
        callApi(callNotifyUnread!!,
            onSuccess = {
                isLoading.value = false
                unreadObserve.value = it
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

    fun setLanguageChange(language: String) {
        isLoading.value = true
        changeLanguage = getRetrofitBuilder().changeLanguage(language)
        callApi(changeLanguage!!,
            onSuccess = {
                isLoading.value = false
                languageObserver.value = it
            },
            onFailure = { throwable, _, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
//                else {
//                    errorResponseObserver.value =
//                        getErrorResponse(statusCode, errorBody)
//                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun getSliderData(slideType: Int) {
        callSlider = getRetrofitBuilder().getHomeSlidersImages(slideType)
        callApi(callSlider!!,
            onSuccess = {
                isLoading.value = false
                sliderObserver.value = it
            },
            onFailure = { throwable, _, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
//                else {
//                    errorResponseObserver.value =
//                        getErrorResponse(statusCode, errorBody)
//                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun doSearch(filter: Map<String, String>) {
        callSearch = getRetrofitBuilder().serach(filter)
        callApi(callSearch!!,
            onSuccess = {
                isLoading.value = false
                searchObserver.value = it
            },
            onFailure = { throwable, _, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
//                else {
//                    errorResponseObserver.value =
//                        getErrorResponse(statusCode, errorBody)
//                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })

    }

    fun saveSearch(searchString: String) {
        callSaveSearch = getRetrofitBuilder().savedSearch(searchString)
        callApi(callSaveSearch!!,
            onSuccess = {
                isLoading.value = false
                saveSearchObserver.value = it
            },
            onFailure = { throwable, _, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
//                else {
//                    errorResponseObserver.value =
//                        getErrorResponse(statusCode, errorBody)
//                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun getAllCategories() {
        isLoadingAllCategory.value = true
        callAllCategories = getRetrofitBuilder().getAllCategories()
        callApi(callAllCategories!!,
            onSuccess = {
                isLoadingAllCategory.value = false
                categoriesObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoadingAllCategory.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    categoriesErrorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoadingAllCategory.value = false
                needToLogin.value = true
            })
    }

    fun getListHomeCategoryProduct() {
        callListHomeCategoryProduct = getRetrofitBuilder().listHomeCategoryProduct()
        callApi(callListHomeCategoryProduct!!,
            onSuccess = {
                homeCategoryProductObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    homeCategoryProductErrorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun getLastViewedProduct() {
        callLastViewedProduct = getRetrofitBuilder().getListLastView()
        callApi(callLastViewedProduct!!,
            onSuccess = {
                lastViewProductsObserver.value = it
            },
            onFailure = { throwable, _, errorBody ->
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
//                else {
//                    categoriesErrorResponseObserver.value =
//                        getErrorResponse(statusCode, errorBody)
//                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun closeAllCall() {
        if (callSlider != null) {
            callSlider?.cancel()
        }
        if (callSearch != null) {
            callSearch?.cancel()
        }
        if (callSaveSearch != null) {
            callSaveSearch?.cancel()
        }
        if (callAllCategories != null) {
            callAllCategories?.cancel()
        }
        if (callListHomeCategoryProduct != null) {
            callListHomeCategoryProduct?.cancel()
        }
        if (callLastViewedProduct != null) {
            callLastViewedProduct?.cancel()
        }
        if(changeLanguage !=null){
            changeLanguage?.cancel()
        }
        if(callNotifyUnread !=null){
            callNotifyUnread?.cancel()
        }

    }
}