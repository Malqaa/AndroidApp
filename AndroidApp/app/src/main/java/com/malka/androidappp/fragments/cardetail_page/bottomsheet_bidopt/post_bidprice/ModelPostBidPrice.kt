package com.malka.androidappp.fragments.cardetail_page.bottomsheet_bidopt.post_bidprice

data class ModelPostBidPrice(
    val auctionId: String,
    val autoBid: String,
    val bidAmount: String,
    val loggedInUserId: String,
    val reminder: String,
    val shippingOption: Int
)