package com.malqaa.androidappp.newPhase.domain.models.walletBalance

import com.google.gson.annotations.SerializedName

data class GetWalletBalanceResponse(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data") val walletBalance: WalletBalance? = null
)

data class WalletBalance(
    var walletBalance: Double? = 0.0,
    @SerializedName("walletTransactionslist") var walletTransactionsList: Double? = 0.0
)