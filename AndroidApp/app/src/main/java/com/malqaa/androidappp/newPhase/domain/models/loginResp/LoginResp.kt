package com.malqaa.androidappp.newPhase.domain.models.loginResp

import com.google.gson.annotations.SerializedName

data class LoginResp (
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val userObject: LoginUser?=null
)
data class LogoutResp (
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val data: String?=null
)