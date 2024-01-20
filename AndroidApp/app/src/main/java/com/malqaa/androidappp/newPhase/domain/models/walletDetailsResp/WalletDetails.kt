package com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp

data class WalletDetails(
    val walletBalance: Double,
    val walletTransactionslist: List<WalletTransactionsDetails>?=null
)