package com.malka.androidappp.newPhase.presentation.searchProductListActivity

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class CategoryProductViewModel : BaseViewModel() {
    var productListRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
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

    ) {
    //    println("tttt "+queryString)
        if(page==1){
            isLoading.value = true
        }else{
            isloadingMore.value = true
        }

//        val data: HashMap<String, String> = HashMap()
//        data["mainCatId"]=categoryId.toString()
//        data["PageRowsCount"]=10.toString()
//        data["pageIndex"]=page.toString()
//        data["lang"]=currentLanguage
        var stringUrl="AdvancedFilter?mainCatId=${categoryId}&lang=${currentLanguage}&PageRowsCount=50&pageIndex=${page}"
        subCategoryList.forEach { item->
            stringUrl+="&subCatIds=${item}"
        }
        countryList.forEach { item->
            stringUrl+="&Countries=${item}"
        }
        regionList.forEach { item->
            stringUrl+="&Regions=${item}"
        }
        neighoodList.forEach { item->
            stringUrl+="&Neighborhoods=${item}"
        }
        specificationList.forEach { item->
            stringUrl+="&sepNames=${item}"
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
            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value = false
                }

                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value = false
                    if (response.isSuccessful) {
                        productListRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}