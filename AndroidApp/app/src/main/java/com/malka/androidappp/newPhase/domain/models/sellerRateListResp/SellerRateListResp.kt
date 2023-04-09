package com.malka.androidappp.newPhase.domain.models.sellerRateListResp

import com.malka.androidappp.newPhase.domain.models.ratingResp.RateReviewItem

data class SellerRateListResp(
    val `data`: List<SellerRateItem>,
    val message: String,
    val status_code: Int
)
data class SellerRateItem(
    var id: Int,
    var imgProfile: String?=null,
    var userName: String?=null,
    var comment: String?=null,
    var rate: Float

)