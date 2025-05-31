package com.malka.androidappp.servicemodels.creditcard

data class CreditCardResponseModel(
    val id: String,
    val cardnumber: String,
    val expiryDate: String,
    val cvcNumber: Int,
    val userId: String,
    val cardType: String,
    val createdby: String,
    val createdOn: String,
    val updateby: String,
    val updatedOn: String,
    val isActive: Boolean,
    var isSelected: Boolean
)