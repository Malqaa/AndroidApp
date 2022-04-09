package com.malka.androidappp.servicemodels.creditcard

data class CreditCardModel(
    val id: String,
    var cardnumber: String,
    val expiryDate: String,
    val cvcNumber: String,
    val userId: String,
    val cardType: String,
    val createdby: String,
    val createdOn: String,
    val updateby: String,
    val updatedOn: String,
    val isActive: Boolean,
    var isSelected: Boolean=false,
    val card_holder_name: String
)
{
    fun cardnumberformat(): String{
       return cardnumber.replace(" ", "")
    }
}