package com.malka.androidappp.newPhase.domain.models.productResp

data class RequestBidOffers(
    val offerExpireHours: Float,
    val price: Float,
    val productId: Int,
    val quantity: Int,
    val userIds: List<String>
)