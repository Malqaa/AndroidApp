package com.malka.androidappp.activities_main.signup_account.signup_pg1

import java.io.Serializable

data class RegisterData(
    val code: String,
    val id: String,
    val message: String,
    val status_code: Int,
    val isError: Boolean,
    val data: List<String>
):Serializable
