package com.malka.androidappp.newPhase.domain.models.walletDetailsResp

data class WalletTransactionsDetails(
    val businessAccountId: String?=null,
    val id: Int,
    val totalWalletBalance: Double,
    val transactionAmount: Double,
    val transactionDate: String?=null,
    val transactionSource: String?=null,
    val transactionType: String?=null,
    val userId: String?=null
)