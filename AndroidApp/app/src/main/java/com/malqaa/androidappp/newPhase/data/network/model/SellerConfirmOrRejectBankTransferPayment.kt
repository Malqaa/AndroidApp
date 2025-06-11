package com.malqaa.androidappp.newPhase.data.network.model

data class SellerConfirmOrRejectBankTransferPaymentRequest(
    val orderId: Int,
    val confirmed: Boolean,
    val comment: String?= null
)
