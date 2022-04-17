package com.malka.androidappp.servicemodels

import com.malka.androidappp.activities_main.signup_account.signup_pg3.User

data class getCartModel(
    val `data`: List<Data>,
    val message: String,
    val status_code: Int
) {
    data class Data(
        val _id: String,
        val advertisements: AdDetailModel,
        val createddate: String,
        val isActive: Boolean,
        val loggenIn: String,
        val sellerId: Any,
        val user: User
    )
}