package com.malka.androidappp.servicemodels.addtocart

import com.malka.androidappp.servicemodels.User

data class CartItemModel(
    val _id: String,
    val loggenIn: String,
    val user: User,
    val advertisements: CartAdObject,
    val createddate: String,
    val isActive: Boolean,
    val sellerId: String,
)