package com.malqaa.androidappp.newPhase.domain.models.showProductModel

import com.google.gson.annotations.SerializedName
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product

data class ShowProductPriceResp(
    val code: String?=null,
    val message: String="",
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val data:Boolean?=null
)