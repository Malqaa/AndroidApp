package com.malqaa.androidappp.newPhase.domain.models

import com.google.gson.annotations.SerializedName
import com.malqaa.androidappp.newPhase.core.BaseViewModel

data class ErrorResponse (
    @SerializedName("status_code")
    val statusCode: Int?=0,
    @SerializedName("message")
    var message: String?="",
    @SerializedName("status")
    val status: String?="",
    @SerializedName("error")
    val error: String?="",
    @SerializedName("result")
    val result: String?="",
    @SerializedName("Message")
    val message2: String?="",
    val data:Any?=null,
)


data class ErrorAddOrder(
    val produtId: Int,
    val productName: String,
    val quantity: Int
)