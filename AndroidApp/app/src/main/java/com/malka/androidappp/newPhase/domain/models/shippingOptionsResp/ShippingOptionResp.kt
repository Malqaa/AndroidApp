package com.malka.androidappp.newPhase.domain.models.shippingOptionsResp

import com.google.gson.annotations.SerializedName

data class ShippingOptionResp(
    @SerializedName("data")
    val shippingOptionObject: List<ShippingOptionObject>?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)