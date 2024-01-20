package com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID

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
    val businessAcountId: String? = "",
    val providerId: String? = "",
    val productName: String? = null,
    val productSpecifications: List<ProductSpecification>? = null,
    val paymentOptions: List<Int>? = null,
    val shippingOptions: List<Int>? = null,
    val paymentOption: String? = null,
    val shippingOption: String? = null,

    val productBankAccountsDto: List<ProductBankAccountsDto>? = null,
    val providerName: String? = null,
    val quantity: Int,
    val region: String? = null,
    var rate: Int = 0,
    var comment: String? = null,
    var productRateId: Int = 0
) : Parcelable

//"midea": [
//{
//    "id": 357,
//    "url": "http://onrufwebsite2-001-site1.btempurl.com/images/Midea/4a85a3ed2d3946059d3a06696ef822f2.jpg",
//    "type": 1,
//    "isMainMadia": true,
//    "productId": 553,
//    "product": null,
//    "createdAt": "2023-09-25T16:42:39.8613704"
//}
//],]
