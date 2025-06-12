package com.malqaa.androidappp.newPhase.domain.models.winningBidsResponse

import com.google.gson.annotations.SerializedName

data class WinningBidsResponse(
    val status_code: Int,
    val message: String,
    val status: String,
    @SerializedName("data")
    val bidModel: List<BidModel>?=null
)
