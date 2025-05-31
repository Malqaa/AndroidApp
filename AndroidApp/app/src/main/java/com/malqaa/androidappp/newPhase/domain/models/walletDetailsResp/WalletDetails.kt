package com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp

data class WalletDetails(
    val walletBalance: Double,
    val pendingBalance : Double,
    val walletTransactionslist: List<WalletTransactionsDetails>?=null,
    val walletPendingOrders : ArrayList<WalletPendingOrders>
)