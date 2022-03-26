package com.malka.androidappp.activities_main.login

data class LoginData(
    val firstName: String,
    val id: String,
    val jwtToken: String,
    val lastName: String,
    val name: String,
    val refreshToken: String,
    val isBusinessUser: Int

){
    val fullName:String
        get() {
            return "$firstName $lastName"
        }
}