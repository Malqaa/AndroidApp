package com.malka.androidappp.servicemodels.addtocart

data class CartItemModel(
    val _id: String,
    val loggenIn: String,
    val user: CartUserObject,
    val advertisements: CartAdObject,
    val createddate: String,
    val isActive: Boolean,
    val sellerId: String,
)