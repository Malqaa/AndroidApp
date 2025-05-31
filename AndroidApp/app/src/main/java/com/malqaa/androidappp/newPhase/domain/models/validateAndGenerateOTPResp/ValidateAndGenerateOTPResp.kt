package com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp

import com.google.gson.annotations.SerializedName

data class ValidateAndGenerateOTPResp(
    @SerializedName("data")
    var otpData: OtpData?=null,
    var message: String?=null,
    var status: String?=null,
    var status_code: Int
)