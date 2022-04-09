package com.malka.androidappp.servicemodels.addtocart

import com.malka.androidappp.activities_main.signup_account.signup_pg3.User

data class CartItemModel(
    val _id: String,
    val loggenIn: String,
    val user: User,
    val advertisements: CartAdObject,
    val createddate: String,
    val isActive: Boolean,
    val sellerId: String,
)