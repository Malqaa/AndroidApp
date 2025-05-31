package com.malqaa.androidappp.newPhase.domain.models.servicemodels.addtocart

import com.malqaa.androidappp.newPhase.domain.models.servicemodels.User

data class CartItemModel(
    val _id: String,
    val loggenIn: String,
    val user: User,
    val advertisements: CartAdObject,
    val createddate: String,
    val isActive: Boolean,
    val sellerId: String,
)