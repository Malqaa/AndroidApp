package com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp

import com.google.gson.annotations.SerializedName

data class UserPointDataResp(
    @SerializedName("data")
    val userPointData: UserPointData?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)