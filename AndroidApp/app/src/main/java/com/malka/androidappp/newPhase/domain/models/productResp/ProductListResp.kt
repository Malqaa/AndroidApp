package com.malka.androidappp.newPhase.domain.models.productResp

import com.google.gson.annotations.SerializedName

data class ProductListResp(
    val code: String?=null,
    val message: String="",
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val productList: List<Product>?=null
)