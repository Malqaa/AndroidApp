package com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID

import com.google.gson.annotations.SerializedName

data class OrderDetailsByMasterIDData(
    // تفاصيل الطلب كله
    val clientName: String?=null,
    @SerializedName("orderFullInfoDto")
    val orderFullInfoDtoList: List<OrderFullInfoDto>?=null,
    val orderMasterId: Int,
    val providersCount: Int,
    val orderStatus :Int,
    val status :String,
    val paymentType :String,
    val paymentTypeId :Int,
    val requestType :String?=null,
    val totalOrderMasterAmountAfterDiscount: Float?=null,
    val totalOrderMasterAmountBeforDiscount: Float?=null
)