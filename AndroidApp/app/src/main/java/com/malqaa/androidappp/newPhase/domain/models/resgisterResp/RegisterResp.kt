package com.malqaa.androidappp.newPhase.domain.models.resgisterResp

import com.google.gson.annotations.SerializedName
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser

data class RegisterResp (
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val userObject: LoginUser
)