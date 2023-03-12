package com.malka.androidappp.newPhase.models.resgisterResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.models.LoginUser

data class RegisterResp (
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val userObject:LoginUser
)