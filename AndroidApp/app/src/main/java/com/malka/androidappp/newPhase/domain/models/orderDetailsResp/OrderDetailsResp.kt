package com.malka.androidappp.newPhase.domain.models.orderDetailsResp

import com.google.gson.annotations.SerializedName

data class OrderDetailsResp(
    @SerializedName("data")
    val orderAllObject: OrderAllObject,
    val message: String,
    val status: String,
    val status_code: Int
)