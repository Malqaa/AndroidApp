package com.malka.androidappp.newPhase.presentation.searchProductListActivity

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.CategoryFollowResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListSearchResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class CategoryProductViewModel : BaseViewModel() {
    var productListRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var searchProductListRespObserver: MutableLiveData<ProductListSearchResp> = MutableLiveData()
    var categoryFollowRespObserver: MutableLiveData<CategoryFollowResp> = MutableLiveData()
    fun searchForProduct(
        categoryId: Int,
        currentLanguage: String,
        page: Int,
        countryList: List<Int>,
        regionList: List<Int>,
        neighoodList: List<Int>,
        subCategoryList: List<Int>,
        specificationList: List<String>,
        startPrice: Float,
        endProce: Float,
        productName: String?,
        comeFrom: Int
    ) {
    //    println("tttt "+queryString)
        if(page==1){
            isLoading.value = true
        } else {
            isloadingMore.value = true
        }

//        val data: HashMap<String, String> = HashMap()
//        data["mainCatId"]=categoryId.toString()
//        data["PageRowsCount"]=10.toString()
//        data["pageIndex"]=page.toString()
//        data["lang"]=currentLanguage
        var stringUrl =
            "AdvancedFilter?lang=${currentLanguage}&PageRowsCount=5&pageIndex=${page}&Screen=$comeFrom"
        if (categoryId != 0) {
            stringUrl += "&mainCatId=${categoryId}"
        }
        if (productName != null) {
            stringUrl += "&productName=${productName} "
        }
        subCategoryList.forEach { item ->
            stringUrl += "&subCatIds=${item} "
        }
        countryList.forEach { item ->
            stringUrl += "&Countries=${item}"
        }
        regionList.forEach { item ->
            stringUrl += "&Regions=${item}"
        }
        neighoodList.forEach { item ->
            stringUrl+="&Neighborhoods=${item}"
        }
        specificationList.forEach { item->
            if (item != null)
                stringUrl += "&sepNames=${item}"
        }
        if(startPrice!=0f){
            stringUrl+="&priceFrom=${startPrice}"
        }
        if(endProce!=0f){
            stringUrl+="&priceTo=${endProce}"
        }

        println("hhhh "+stringUrl)
        RetrofitBuilder.GetRetrofitBuilder()
            .searchForProductInCategory(stringUrl)
            .enqueue(object : Callback<ProductListSearchResp> {
                override fun onFailure(call: Call<ProductListSearchResp>, t: Throwable) {
                    println("hhhh " + t.message)
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value = false
                }

                override fun onResponse(
                    call: Call<ProductListSearchResp>,
                    response: Response<ProductListSearchResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value = false
                    println("hhhh  t " + response.code() + " " + Gson().toJson(response.body()))
                    if (response.isSuccessful) {
                        searchProductListRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getCategoryFollow(){
        RetrofitBuilder.GetRetrofitBuilder()
            .getListCategoryFollow()
            .enqueue(object : Callback<CategoryFollowResp> {
                override fun onFailure(call: Call<CategoryFollowResp>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<CategoryFollowResp>,
                    response: Response<CategoryFollowResp>
                ) {
                    if (response.isSuccessful) {
                        categoryFollowRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getMyBids() {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getMyBids()
            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        productListRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}