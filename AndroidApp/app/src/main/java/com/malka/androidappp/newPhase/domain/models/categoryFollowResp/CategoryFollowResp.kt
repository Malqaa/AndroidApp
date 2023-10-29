package com.malka.androidappp.newPhase.domain.models.categoryFollowResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.addRateResp.AddRateItem

data class CategoryFollowResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val CategoryFollowList: List<CategoryFollowItem>? = null
)

data class CategoryFollowItem(
    var id: Int,
    var name: String? = null,
    var description: String? = null,
    var postion: Int,
    var isShowInHome: Boolean,
    var image: String? = null,
    var parentId: Int,
    var isActive: Boolean,
    var isFollow: Boolean,
    var productPriceType: Int,
    var productPublishPrice: Int,
    var productFeeDuetTime: Int,

    val auctionClosingPeriods: Any,
    val auctionClosingPeriodsUnit: Any,
    val auctionClosingTimeFee: Any,
    val enableAuction: Boolean,
    val enableAuctionFee: Int,
    val enableFixedPriceSaleFee: Int,
    val enableNegotiation: Boolean,
    val enableNegotiationFee: Int,
    val extraProductImageFee: Any,
    val extraProductVidoeFee: Any,
    val freeProductImagesCount: Any,
    val freeProductVidoesCount: Any,
    val isRealState: Boolean,
    val list: Any,
    val minimumBidValue: Any,
    val subTitleFee: Any

    )
