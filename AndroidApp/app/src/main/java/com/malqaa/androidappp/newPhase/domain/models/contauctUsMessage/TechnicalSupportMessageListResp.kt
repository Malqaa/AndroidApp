package com.malqaa.androidappp.newPhase.domain.models.contauctUsMessage

import com.google.gson.annotations.SerializedName

data class TechnicalSupportMessageListResp(
    @SerializedName("data")
    val technicalSupportMessageList: List<TechnicalSupportMessageDetails>?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)