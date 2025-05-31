package com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp

import com.google.gson.annotations.SerializedName

data class TransferWalletData(
    val businessAccountId: String="",
    val userId:String="",
    val transactionDate: String="",
    val transactionSource: String="",
    val transactionType: String="",
    val transactionAmount: Int,
    val totalWalletBalance: Int,
)
