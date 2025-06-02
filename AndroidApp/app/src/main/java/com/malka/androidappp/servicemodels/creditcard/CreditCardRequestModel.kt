package com.malka.androidappp.servicemodels.creditcard

data class CreditCardRequestModel(
    val card_number: String,
    val expiryDate: String,
    val cvcNumber: Int,
    val userId: String,
    val id: String
)