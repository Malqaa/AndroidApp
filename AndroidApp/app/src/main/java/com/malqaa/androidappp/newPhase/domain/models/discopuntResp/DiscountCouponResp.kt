package com.malqaa.androidappp.newPhase.domain.models.discopuntResp

import com.google.gson.annotations.SerializedName

data class DiscountCouponResp(
    @SerializedName("data")
    val discountCouponObject: DiscountCouponObject?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)