package com.malqaa.androidappp.newPhase.domain.models.addWaletTransactionResp

import com.google.gson.annotations.SerializedName

data class AddWalletTranactionResp(
    @SerializedName("AddWalletTranactionWallet")
    val addWalletTranactionWalletDetails: AddWalletTranactionWalletDetails?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)