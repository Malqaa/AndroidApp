package com.malka.androidappp.newPhase.domain.models.orderDetailsResp

data class OrderFullInfoDto(
    val branchId: Any,
    val businessAcountName: String,
    val createdAt: String,
    val orderId: Int,
    val orderProductFullInfoDto: List<OrderProductFullInfoDto>,
    val orderSaleType: Int,
    val orderStatus: Int,
    val phoneNumber: Any,
    val providerName: String,
    val shippingAddress: Any,
    val shippingCount: Int,
    val totalOrderPrice: Int
)