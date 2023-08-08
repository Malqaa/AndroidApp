package com.malka.androidappp.newPhase.domain.models.shippingOptionsResp

data class ShippingOptionObject(
    /**shipping options*/
    val shippingOptionId: Int,
    val shippingOptionName: String? = null,
    /**paymentOption*/
    val paymentOptionId: Int,
    val paymentOption: String? = null
)