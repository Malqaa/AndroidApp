package com.malqaa.androidappp.newPhase.domain.models.addBidResp

import com.google.gson.annotations.SerializedName

data class AddBidResp(
    @SerializedName("data")
    val BidObject: BidObject?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)