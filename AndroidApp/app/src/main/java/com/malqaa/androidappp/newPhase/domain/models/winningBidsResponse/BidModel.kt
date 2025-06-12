package com.malqaa.androidappp.newPhase.domain.models.winningBidsResponse

data class BidModel(
    val bidId: Int,
    val subTitle: String?, // ممكن تكون null
    val auctionClosingTime: String?=null,
    val productCategory: String,
    val productId: Int,
    val productName: String,
    val sellerName: String,
    val productImage: String?,
    val senderImage: String?, // nullable
    val sellerImage: String,
    val productPrice: Int,
    val bidPrice: Long,
    val bidQuantity: Int,
    val region: String,
    val sellerRegion: String,
    val isBidFinished: Boolean,
    val bidWinningCartMasterId: Int?, // nullable
    val bidDate: String,
    val isMerchant: Boolean,
    val auctionStatus: Int,
    val auctionStatusName: String
)
