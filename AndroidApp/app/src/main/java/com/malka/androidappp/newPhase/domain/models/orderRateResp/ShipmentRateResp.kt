package com.malka.androidappp.newPhase.domain.models.orderRateResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderItem

data class ShipmentRateResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val rateObject: RateObject? = null
)

data class RateObject(
    var orderId: Int,
    var sellerDateDto: SellerDateDto ,
    var productsRate: List<RateProductObject>,
    var shippmentRateDto: ShippmentRateDto,

    )
data class RateProductObject(
    var productRateId: Int,
    var productId: Int,
    var rate: Int,
    var comment: String,
)
data class SellerDateDto(
    var sellerRateId: Int,
    var sellerId: String,
    var businessAccountId:Int,
    var rate: Int ,
    var comment: String ,
)

data class ShippmentRateDto(
    var shippmentRateId: Int ,
    var shippmentId: Int,
    var rate: Int ,
    var comment: String ,
)
