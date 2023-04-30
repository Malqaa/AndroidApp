package com.malka.androidappp.newPhase.domain.models.sellerRateListResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateReviewItem

data class SellerRateListResp(
    @SerializedName("data")
    val SellerRateObject: SellerRateObject? = null,
    val message: String,
    val status_code: Int
)

data class SellerRateObject(
    var disgustedCount: Int,
    var satisfiedCount: Int,
    var happyCount: Int,
    var rateSellerListDto: List<SellerRateItem>? = null,
    var totalRecords: Int
)

data class SellerRateItem(
    var id: Int,
    var imgProfile: String? = null,
    var userName: String? = null,
    var comment: String? = null,
    var rate: Float

)




