package com.malka.androidappp.activities_main.login

data class LoginResponseBack(
    val `data`: LoginData,
    val message: String,
    val status_code: Int
)