package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.service.ListenerCallBackNeighborhoods
import com.malqaa.androidappp.newPhase.data.network.service.ListenerCallBackRegions
import com.malqaa.androidappp.newPhase.data.network.service.ListenerCallDynamicSpecification
import com.malqaa.androidappp.newPhase.data.network.service.MalqaApiService
import com.malqaa.androidappp.newPhase.domain.models.ErrorResponse
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.AddFollowObj
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.CategoryFollowResp
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductListSearchResp
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.Region
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.RegionsResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.android.synthetic.main.dialog_filter_category_products.progressBar
import kotlinx.android.synthetic.main.dialog_filter_category_products.tvError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class CategoryProductViewModel : BaseViewModel() {
    var productListRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var searchProductListRespObserver: MutableLiveData<ProductListSearchResp> = MutableLiveData()
    var saveSearchObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoryFollowRespObserver: MutableLiveData<CategoryFollowResp> = MutableLiveData()
    var isFollowCategory: MutableLiveData<Boolean> = MutableLiveData()


    private var callSearchForProduct: Call<ProductListSearchResp>? = null
    private var callListCategoryFollow: Call<CategoryFollowResp>? = null
    private var callSaveSearch: Call<GeneralResponse>? = null
    private var callMyBids: Call<ProductListResp>? = null
    private var callFollow: Call<GeneralResponse>? = null
    private var regionsCallback: Call<RegionsResp>? = null
    private var neighborhoodsCallback : Call<RegionsResp>? = null
    private var dynamicSpecificationCallback : Call<DynamicSpecificationResp>? = null
    /***get Specification**/
    fun getDynamicSpecification(categoryId: Int,listener: ListenerCallDynamicSpecification) {
        dynamicSpecificationCallback = getRetrofitBuilder()
            .getDynamicSpecificationForCategory(categoryId.toString())
        dynamicSpecificationCallback?.enqueue(object : Callback<DynamicSpecificationResp> {
            override fun onFailure(call: Call<DynamicSpecificationResp>, t: Throwable) {

                listener.callBackDynamicSpecification(true,null)
            }

            override fun onResponse(
                call: Call<DynamicSpecificationResp>,
                response: Response<DynamicSpecificationResp>
            ) {
                if (response.isSuccessful) {
                        listener.callBackDynamicSpecification(false,response.body()!!)
                }
            }
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

    fun getNeighborhoods(cityId: Int, listener: ListenerCallBackNeighborhoods) {
         neighborhoodsCallback = getRetrofitBuilder().getNeighborhoodByRegionNew(cityId)
        neighborhoodsCallback?.enqueue(object : Callback<RegionsResp> {
            override fun onFailure(call: Call<RegionsResp>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<RegionsResp>,
                response: Response<RegionsResp>
            ) {

                if (response.isSuccessful) {
                    listener.callBackListenerNeighborhoods(false, response.body())
                }

            }

        })
    }


    fun getRegions(countryId: Int, listener: ListenerCallBackRegions, context: Context) {
        regionsCallback = getRetrofitBuilder().getRegionNew(countryId)
        regionsCallback?.enqueue(object : Callback<RegionsResp> {
            override fun onFailure(call: Call<RegionsResp>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<RegionsResp>,
                response: Response<RegionsResp>
            ) {

                if (response.isSuccessful) {
                    listener.callBackListener(false, response.body())
                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.serverError),
                        context
                    )

                }
            }

        })
    }

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

    fun removeFollow(categoryID: Int, context: Activity) {
        HelpFunctions.startProgressBar(context)

        callFollow = getRetrofitBuilder().removeFollow(categoryID)
        callApi(callFollow!!,
            onSuccess = {
                HelpFunctions.dismissProgressBar()
                println("tttt add " + Gson().toJson(it))
                isFollowCategory.value = false
            },
            onFailure = { throwable, statusCode, errorBody ->
                HelpFunctions.dismissProgressBar()
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

    fun followCategoryAPI(categoryID: Int, context: Activity) {
        HelpFunctions.startProgressBar(context)
        callFollow = getRetrofitBuilder().addFollow(
            AddFollowObj(arrayListOf(categoryID))
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
                        isFollowCategory.value = true
                    }
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
        if (callSaveSearch != null) {
            callSaveSearch?.cancel()
        }

        if (callListCategoryFollow != null) {
            callListCategoryFollow?.cancel()
        }
        if (dynamicSpecificationCallback != null) {
            dynamicSpecificationCallback?.cancel()
        }
        if (callMyBids != null) {
            callMyBids?.cancel()
        }
        if (callFollow != null) {
            callFollow?.cancel()
        }
        if (regionsCallback != null) {
            regionsCallback?.cancel()
        }
        if (neighborhoodsCallback != null) {
            neighborhoodsCallback?.cancel()
        }

    }
}