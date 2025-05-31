package com.malqaa.androidappp.newPhase.domain.models.pointsBalance

import com.google.gson.annotations.SerializedName

data class GetPointsBalanceResponse(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data") val pointsBalance: PointsBalance? = null
)

data class PointsBalance(
    var pointsBalance: Int? = 0,
    var pointsCountToTransfer: Int? = 0,
    @SerializedName("monyOfPointsTransfered") var moneyOfPointsTransferred: Double? = 0.0
)