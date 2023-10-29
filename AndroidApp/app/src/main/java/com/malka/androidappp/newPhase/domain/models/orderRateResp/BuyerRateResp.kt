package com.malka.androidappp.newPhase.domain.models.orderRateResp

import com.google.gson.annotations.SerializedName

data class BuyerRateResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val rateObject: BuyerRateDto? = null
)


data class BuyerRateDto(
    var orderId: Int ,
    var buyerRateId: Int ,
    var buyerId: String,
    var rate: Int ,
    var comment: String ,
)