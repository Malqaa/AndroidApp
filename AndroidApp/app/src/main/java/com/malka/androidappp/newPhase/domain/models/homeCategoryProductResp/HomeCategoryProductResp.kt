package com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp

import com.google.gson.annotations.SerializedName

data class HomeCategoryProductResp (
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    var categoryProductList:List<CategoryProductItem> ?=null
)