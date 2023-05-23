package com.malka.androidappp.newPhase.domain.models.sellerInfoResp

import com.google.gson.annotations.SerializedName

data class SellerInfoResp(
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val sellerInformation: SellerInformation?=null,
)