package com.malka.androidappp.newPhase.domain.models.bidPersonsResp

import com.google.gson.annotations.SerializedName

data class BidPersonsResp(
    @SerializedName("data")
    val bidPersonsDataList: ArrayList<BidPersonData>?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)