package com.malka.androidappp.activities_main.product_detail

data class RateResponse(
    val `data`: List<RateReview>,
    val message: String,
    val status_code: Int
) {
    data class RateReview(
        val comment: String,
        val createdAt: String,
        val id: Int,
        val productNameAr: String,
        val productNameEn: String,
        val rate: String,
        val userName: String
    )
}