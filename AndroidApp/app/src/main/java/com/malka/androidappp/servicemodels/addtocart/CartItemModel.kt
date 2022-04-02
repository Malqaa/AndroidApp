package com.malka.androidappp.servicemodels.addtocart

import com.malka.androidappp.servicemodels.AdDetailModel

data class CartItemModel(
    val _id: String,
    val loggenIn: String,
    val user: CartUserObject,
    val advertisements: CartAdObject,
   // val advertisements: AdDetailModel,
    val createddate: String,
    val isActive: Boolean,
    val sellerId: String,
)