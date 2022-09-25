package com.malka.androidappp.servicemodels.model

data class HomeProducts(
    val date: String,
    val id: Int,
    val image: String,
    val isActive: Boolean,
    val isPaied: Boolean,
    val name: String,
    val price: Int,
    val productId: Int,
    val qty: Int
)