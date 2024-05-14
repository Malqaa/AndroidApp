package com.malqaa.androidappp.newPhase.domain.models.servicemodels

import com.google.gson.annotations.SerializedName


data class GeneralResponse(
    val `data`: Any?=null,
    val message: String="",
    val status_code: Int=0,
    val status: String="",
    var addressTitle:String=""
)


data class AddProductResponse(
    @SerializedName("data")
    val productId: Int,
    val message: String,
    val status_code: Int
)

data class PurchaseResponse(
    val orderMasterId: Double,
    val status: Boolean
)