package com.malka.androidappp.newPhase.domain.models.orderDetails

import com.google.gson.annotations.SerializedName

data class OrderDetailsResp(
    @SerializedName("data")
    val orderDetails: OrderDetailsData,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)