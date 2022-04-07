package com.malka.androidappp.servicemodels.addtocart

data class CartAdObject(
    val categoryName: String,
    val cityName: String,
    val id: String,
    val image: String,
    val isbankpaid: String,
    val iscashpaid: String,
    val listingtitle: String,
    val name: String,
    val pickupOption: String,
    val price: String,
    val producttitle: String,
    val sellerId: String,
    val sellername: String,
    val shippingOption: String,
    val subCategory1: String,
    val subCategory2: String,
    val subCategory3: String,
    val subCategory4: String,
    val subCategory5: String,
    val subCategory6: String,
    val userBankAccountIDs: String,
    var qty: String
)