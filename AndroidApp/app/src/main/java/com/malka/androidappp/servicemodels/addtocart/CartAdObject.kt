package com.malka.androidappp.servicemodels.addtocart

data class CartAdObject(
    val id: String,
    val name: String,
    val image: String,
    val listingtitle: String,
    val producttitle: String,
    val price: String,
    val sellerId: String,
    var qty: String = "1"
)