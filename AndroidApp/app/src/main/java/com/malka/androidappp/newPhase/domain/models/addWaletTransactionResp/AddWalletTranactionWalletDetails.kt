package com.malka.androidappp.newPhase.domain.models.addWaletTransactionResp

data class AddWalletTranactionWalletDetails(
    val transactionAmount: Double,
    val transactionSource: String?=null,
    val transactionType: String?=null
)