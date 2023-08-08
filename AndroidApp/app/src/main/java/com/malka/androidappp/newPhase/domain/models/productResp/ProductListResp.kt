package com.malka.androidappp.newPhase.domain.models.productResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category

data class ProductListResp(
    val code: String?=null,
    val message: String="",
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val productList: List<Product>?=null
)

data class ProductListSearchResp(
    val code: String?=null,
    val message: String="",
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val data: SearchProductList?=null

)
data class SearchProductList(
    val products:List<Product>?=null,
    val categories:List<CategoriesSearchItem>?=null
)

data class CategoriesSearchItem(
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    var isSelected: Boolean = false,
    var categoryList: List<CategoriesSearchItem>?=null
)
