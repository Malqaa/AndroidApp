package com.malka.androidappp.servicemodels.addtocart

data class CartUserObject(
    val id: String,
    val username: String,
    val email: String,
    val phone: String,
    val address: String,
    val country: String,
    val state: String,
    val city: String
)