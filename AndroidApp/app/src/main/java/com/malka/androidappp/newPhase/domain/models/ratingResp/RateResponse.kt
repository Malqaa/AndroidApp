package com.malka.androidappp.newPhase.domain.models.ratingResp

data class RateResponse(
    val `data`: List<RateReviewItem>,
    val message: String,
    val status_code: Int
)
data class CurrentUserRateResp(
    val `data`: RateReviewItem?=null,
    val message: String,
    val status_code: Int
)
data class RateReviewItem(
    val id: Int,
    var rate: Float,
    var comment: String,
    val productName: String,
    val userName: String,
    val createdAt: String,
    var image:String
//        val productNameAr: String,
//        val productNameEn: String,



)

