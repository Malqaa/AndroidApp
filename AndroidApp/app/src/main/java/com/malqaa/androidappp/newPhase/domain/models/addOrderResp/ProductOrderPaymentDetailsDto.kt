package com.malqaa.androidappp.newPhase.domain.models.addOrderResp

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ProductOrderPaymentDetailsDto(
    @SerializedName("productId")
    var productId: Int=0,
    @SerializedName("paymentOption")
    var paymentOption: Int=0,
    @SerializedName("shippingOption")
    var shippingOption: Int=0
):Serializable