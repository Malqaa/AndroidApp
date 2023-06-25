package com.malka.androidappp.newPhase.domain.models.cartPriceSummery

import com.google.gson.annotations.SerializedName

data class CartPriceSummeryResp(
    @SerializedName("data")
    val priceSummery: PriceSummery?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)