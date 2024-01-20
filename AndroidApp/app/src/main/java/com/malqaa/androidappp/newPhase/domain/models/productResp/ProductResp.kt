package com.malqaa.androidappp.newPhase.domain.models.productResp

import com.google.gson.annotations.SerializedName

data class ProductResp (
    val code: String?=null,
    val message: String="",
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val productDetails: Product?=null
)