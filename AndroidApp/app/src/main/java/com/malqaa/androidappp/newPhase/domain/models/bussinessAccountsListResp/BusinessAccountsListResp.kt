package com.malqaa.androidappp.newPhase.domain.models.bussinessAccountsListResp

import com.google.gson.annotations.SerializedName

data class BusinessAccountsListResp(
    @SerializedName("data")
    val  businessAccountsList:List<BusinessAccountDetials>?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)