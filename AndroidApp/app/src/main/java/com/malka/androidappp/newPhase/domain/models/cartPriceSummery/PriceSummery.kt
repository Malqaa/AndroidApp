package com.malka.androidappp.newPhase.domain.models.cartPriceSummery

data class PriceSummery(
    val categoryId: Int,
    val enableAuctionFee: Float,
    val enableFixedPriceSaleFee: Float,
    val enableNegotiationFee: Float,
    val pakatId: Int,
    val pakatPrice: Float,
    val productPublishPrice: Float,
    val totalPriceAfterCoupon: Float,
    val totalPriceBeforeCoupon: Float
)