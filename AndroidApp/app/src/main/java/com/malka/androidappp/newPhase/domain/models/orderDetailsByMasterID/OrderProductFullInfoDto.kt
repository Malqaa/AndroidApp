package com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID

import android.os.Parcelable
import com.malka.androidappp.newPhase.domain.models.orderDetails.Midea
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderProductFullInfoDto(
    val businessAcountName: String?=null,
    val category: String?=null,
    val iamge: String?=null,
    val midea: List<Midea>,
    val price: Float,
    val priceDiscount: Float,
    val productId: Int,
    val productName: String?=null,
    val productSpecifications: List<ProductSpecification>?=null,
    val providerName: String?=null,
    val quantity: Int,
    val region: String?=null
):Parcelable