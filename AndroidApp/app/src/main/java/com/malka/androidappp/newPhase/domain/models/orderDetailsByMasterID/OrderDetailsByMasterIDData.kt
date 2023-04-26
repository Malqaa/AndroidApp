package com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID

import com.google.gson.annotations.SerializedName

data class OrderDetailsByMasterIDData(
    val clientName: String?=null,
    @SerializedName("orderFullInfoDto")
    val orderFullInfoDtoList: List<OrderFullInfoDto>?=null,
    val orderMasterId: Int,
    val providersCount: Int,
    val totalOrderMasterAmountAfterDiscount: Float?=null,
    val totalOrderMasterAmountBeforDiscount: Float?=null
)