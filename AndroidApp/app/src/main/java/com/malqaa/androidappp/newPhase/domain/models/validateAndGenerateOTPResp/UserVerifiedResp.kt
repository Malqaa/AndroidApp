package com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp

import com.google.gson.annotations.SerializedName

data class UserVerifiedResp (
    @SerializedName("data")
    var userVerified: Boolean,
    var message: String?=null,
    var status: String?=null,
    var status_code: Int
    )