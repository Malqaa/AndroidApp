package com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp

import com.google.gson.annotations.SerializedName

data class TransactionPointsAmountRes(
    val message: String="",
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val data:TransferWalletData
)
