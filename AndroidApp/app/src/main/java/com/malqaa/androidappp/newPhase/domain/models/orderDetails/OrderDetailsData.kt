package com.malqaa.androidappp.newPhase.domain.models.orderDetails

import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto

data class OrderDetailsData(
    val branchId: Int,
    val totalOrderAmountBeforDiscount: Int,
    val totalOrderAmountAfterDiscount: Int,
    val businessAcountName: String? = null,
    val createdAt: String? = null,
    val orderId: Int,
    val clientId: String? = null,
    val orderProductFullInfoDto: List<OrderProductFullInfoDto>? = null,
    val orderSaleType: String? = null,
    val orderStatus: Int,
    val paymentType: String? = null,
    val phoneNumber: String? = null,
    val providerName: String? = null,
    val requestType: String? = null,
    val shippingAddress: String? = null,
    val shippingCount: Int,
    val status: String? = null,
    val totalOrderPrice: Float
)