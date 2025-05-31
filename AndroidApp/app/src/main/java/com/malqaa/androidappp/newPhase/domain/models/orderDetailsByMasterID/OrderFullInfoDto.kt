package com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderFullInfoDto(
    //ليست الشحنات وجواها تفاصيل المنتج
    val branchId: Int,
    val businessAcountName: String? = null,
    val createdAt: String? = null,
    val orderId: Int,// رقم الشحنه
    val orderProductFullInfoDto: List<OrderProductFullInfoDto>? = null,
    val orderSaleType: String,
    val orderStatus: Int,
    val paymentType: String? = null,
    val paymentTypeId :Int =0,
    //خاصه بالشحنه كلها
    val productBankAccountsDto: List<ProductBankAccountsDto>? = null,
    val phoneNumber: String? = null,
    val providerName: String? = null,
    val providerId: String? = null,
    val businessAcountId: Int? = null,
    val requestType: String? = null,
    val shippingAddress: String? = null,
    val shippingCount: Int,
    val status: String? = null,
    val totalOrderPrice: Float,
    val orderInvoice: String

) : Parcelable