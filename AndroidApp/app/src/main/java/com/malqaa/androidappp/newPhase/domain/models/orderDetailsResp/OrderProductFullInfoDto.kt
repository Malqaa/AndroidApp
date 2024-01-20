package com.malqaa.androidappp.newPhase.domain.models.orderDetailsResp

data class OrderProductFullInfoDto(
    val businessAcountName: String,
    val iamge: String,
    val midea: List<Midea>,
    val price: Int,
    val priceDiscount: Int,
    val productId: Int,
    val productName: String,
    val productSpecifications: List<Any>,
    val providerName: String,
    val quantity: Int
)