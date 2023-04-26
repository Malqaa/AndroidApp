package com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderFullInfoDto(
    val branchId: Int,
    val businessAcountName: String?=null,
    val createdAt: String?=null,
    val orderId: Int,
    val orderProductFullInfoDto: List<OrderProductFullInfoDto>?=null,
    val orderSaleType: Int,
    val orderStatus: Int,
    val payType: String?=null,
    val phoneNumber: String?=null,
    val providerName: String?=null,
    val requestType: String?=null,
    val shippingAddress: String?=null,
    val shippingCount: Int,
    val status: String?=null,
    val totalOrderPrice: Float
):Parcelable