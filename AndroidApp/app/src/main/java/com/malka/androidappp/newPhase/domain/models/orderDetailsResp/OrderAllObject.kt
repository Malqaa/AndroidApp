package com.malka.androidappp.newPhase.domain.models.orderDetailsResp

data class OrderAllObject(
    val clientName: Any,
    val orderFullInfoDto: List<OrderFullInfoDto>,
    val orderMasterId: Int,
    val providersCount: Int,
    val totalOrderMasterAmountAfterDiscount: Float,
    val totalOrderMasterAmountBeforDiscount: Float
)