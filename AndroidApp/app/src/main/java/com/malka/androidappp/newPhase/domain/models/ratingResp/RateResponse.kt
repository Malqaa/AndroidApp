package com.malka.androidappp.newPhase.domain.models.ratingResp

data class RateResponse(
    val `data`: List<RateReviewItem>,
    val message: String,
    val status_code: Int
)
data class RateReviewItem(
    val comment: String,
    val createdAt: String,
    val id: Int,
    val productName: String,
//        val productNameAr: String,
//        val productNameEn: String,
    val rate: Float,
    val userName: String,
    var image:String
)