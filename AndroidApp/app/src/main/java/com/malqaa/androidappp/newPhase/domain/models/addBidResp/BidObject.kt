package com.malqaa.androidappp.newPhase.domain.models.addBidResp

data class BidObject(
    val activateAutomaticBidding: Boolean,
    val bidPrice: Float,
    val highestBidPrice: Float,
    val id: Int,
    val increaseEachTimePrice: Float,
    val productId: Int
)