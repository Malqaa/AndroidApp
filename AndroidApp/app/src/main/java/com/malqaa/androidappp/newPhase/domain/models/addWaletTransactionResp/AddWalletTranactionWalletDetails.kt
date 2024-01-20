package com.malqaa.androidappp.newPhase.domain.models.addWaletTransactionResp

data class AddWalletTranactionWalletDetails(
    val transactionAmount: Double,
    val transactionSource: String?=null,
    val transactionType: String?=null
)