package com.malka.androidappp.activities_main.signup_account.signup_pg1

import okhttp3.ResponseBody
import java.io.Serializable

data class RegisterData(
    val code: String,
    val `data`: String,
    val message: String,
    val status_code: Int
):Serializable
