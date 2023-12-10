package com.malka.androidappp.newPhase.domain.models.shippingOptionsResp

data class ShippingOptionObject(
    /**shipping options*/
    val id: Int,
    val shippingOptionDescription: String? = null,
    val shippingOptionTypeId: Int,
    val shippingOptionName: String? = null,
    val shippingOptionImage: String? = null,
    val isActive: Boolean? = null,
    val createdBy: String? = null,
    val createdAt: String? = null,
    val shippingOptionId: Int,
    var selected: Boolean=false,
    /**paymentOption*/
    val paymentOptionId: Int,
    val paymentOption: String? = null
)