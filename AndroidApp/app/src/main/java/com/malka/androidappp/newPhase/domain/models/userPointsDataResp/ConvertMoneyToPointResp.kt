package com.malka.androidappp.newPhase.domain.models.userPointsDataResp

import com.google.gson.annotations.SerializedName

data class ConvertMoneyToPointResp(
    @SerializedName("data")
    val pointsTransactionsItem: PointsTransactionsItem?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)