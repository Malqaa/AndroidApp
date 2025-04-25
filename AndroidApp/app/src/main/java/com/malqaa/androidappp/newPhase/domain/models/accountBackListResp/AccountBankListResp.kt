package com.malqaa.androidappp.newPhase.domain.models.accountBackListResp

import com.google.gson.annotations.SerializedName
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct.PaymentAccountType

data class AccountBankListResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val accountsList: ArrayList<AccountDetails>? = null
)

data class AccountDetails(
    var id: Int,
    var accountNumber: String? = null,
    var bankAccountId: Int,
    var bankName: String? = null,
    var bankHolderName: String? = null,
    var ibanNumber: String? = null,
    var expiaryDate: String? = null,
    var swiftCode: String? = null,
    var providerId: String? = null,
    var isActive: String? = null,
    var saveForLaterUse: String? = null,
    var isSelected: Boolean,
    var cvv: Int,
    var paymentAccountType: PaymentAccountType? = null
)

data class BankAccountRequest(
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("bankName") val bankName: String? = null,
    @SerializedName("bankHolderName") val bankHolderName: String,
    @SerializedName("ibanNumber") val ibanNumber: String? = null,
    @SerializedName("swiftCode") val swiftCode: String? = null,
    @SerializedName("expiaryDate") val expiryDate: String,
    @SerializedName("SaveForLaterUse") val saveForLaterUse: Boolean,
    @SerializedName("PaymentAccountType") val paymentAccountType: String? = null
)
