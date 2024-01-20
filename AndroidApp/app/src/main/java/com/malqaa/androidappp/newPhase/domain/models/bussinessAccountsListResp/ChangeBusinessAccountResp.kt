package com.malqaa.androidappp.newPhase.domain.models.bussinessAccountsListResp

import com.google.gson.annotations.SerializedName

data class ChangeBusinessAccountResp(
    @SerializedName("data")
    val  businessAccount:BusinessAccountDetials?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)
