package com.malka.androidappp.newPhase.domain.models.orderListResp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import kotlinx.android.parcel.Parcelize

data class OrderListResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val orderList: List<OrderItem>? = null
)
@Parcelize
data class OrderItem(
    var orderId: Int,
    var orderMasterId: Int,
    var totalOrderAmountBeforDiscount: Float,
    var totalOrderAmountAfterDiscount: Float,
    var providersCount: Int,
    var status: String? = null,
    var createdAt: String? = null,
    var orderStatus:Int ,
    var clientName: String? = null,
    var shippingAddress: String? = null,
    var requestType: String? = null,
    var payType: String? = null,
):Parcelable

