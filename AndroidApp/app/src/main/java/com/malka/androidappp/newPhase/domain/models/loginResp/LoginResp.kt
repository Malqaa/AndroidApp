package com.malka.androidappp.newPhase.domain.models.loginResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.LoginUser

data class LoginResp (
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val userObject: LoginUser?=null
)