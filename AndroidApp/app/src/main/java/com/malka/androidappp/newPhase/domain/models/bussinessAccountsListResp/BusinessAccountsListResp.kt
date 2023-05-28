package com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp

import com.google.gson.annotations.SerializedName

data class BusinessAccountsListResp(
    @SerializedName("data")
    val  businessAccountsList:Any?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)