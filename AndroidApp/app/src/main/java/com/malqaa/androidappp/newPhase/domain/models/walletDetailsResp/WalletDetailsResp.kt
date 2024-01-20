package com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp

import com.google.gson.annotations.SerializedName

data class WalletDetailsResp(
    @SerializedName("data")
    val walletDetails: WalletDetails?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)