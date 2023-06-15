package com.malka.androidappp.newPhase.domain.models.accountBackListResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.addOrderResp.AddOrderObject

data class AccountBankListResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val accountsList: List<AccountDetails>? = null
)

data class AccountDetails(
    var id: Int,
    var accountNumber: String? = null,
    var bankName: String? = null,
    var bankHolderName: String? = null,
    var ibanNumber: String? = null,
    var expiaryDate: String? = null,
    var swiftCode: String? = null,
    var providerId: String? = null,
    var isActive: String? = null,
    var saveForLaterUse: String?=null,
    var isSelected:Boolean
)