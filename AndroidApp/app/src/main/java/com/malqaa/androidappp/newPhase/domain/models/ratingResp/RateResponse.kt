package com.malqaa.androidappp.newPhase.domain.models.ratingResp

data class RateResponse(
    val `data`: RateReviewItem,
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
    var image:String,

    val disgustedCount: Int,
    val satisfiedCount: Int,
    val happyCount: Int,
    val totalRecords: Int,
    val rateProductListDto: List<RateReviewItem>,

//        val productNameAr: String,
//        val productNameEn: String,



)

data class RateProductResponse(
    val `data`: RateProductReviewItem,
    val message: String,
    val status_code: Int
)

data class RateProductReviewItem(
    val disgustedCount: Int,
    val satisfiedCount: Int,
    val happyCount: Int,
    val totalRecords: Int,
    val rateProductListDto: List<RateReviewItem>,

    )
