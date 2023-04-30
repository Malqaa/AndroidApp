package com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderProductFullInfoDto(
    val businessAcountName: String? = null,
    val category: String? = null,
    val iamge: String? = null,
    val midea: List<Midea>,
    val price: Float,
    val priceDiscount: Float,
    val productId: Int,
    val productName: String? = null,
    val productSpecifications: List<ProductSpecification>? = null,
    val providerName: String? = null,
    val quantity: Int,
    val region: String? = null,
    var rate: Int = 0,
    var comment: String?=null,
) : Parcelable



data class RateObject(
    var orderMasterId: Int,
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