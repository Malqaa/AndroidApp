package com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID

import com.google.gson.annotations.SerializedName

data class OrderDetailsByMasterIDResp(
    @SerializedName("data")
    val orderDetailsByMasterIDData: OrderDetailsByMasterIDData?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)