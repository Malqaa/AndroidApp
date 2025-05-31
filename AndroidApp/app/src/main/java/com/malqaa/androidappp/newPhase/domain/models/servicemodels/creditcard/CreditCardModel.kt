package com.malqaa.androidappp.newPhase.domain.models.servicemodels.creditcard

data class CreditCardModel(
    val id: String?=null,
    var cardnumber: String?=null,
    val expiryDate: String?=null,
    val cvcNumber: String?=null,
    val userId: String?=null,
    val cardType: String?=null,
    val createdby: String?=null,
    val createdOn: String?=null,
    val updateby: String?=null,
    val updatedOn: String?=null,
    val isActive: Boolean?=null,
    var isSelected: Boolean=false,
    val card_holder_name: String?=null,
    val card_number: String?=null,
)
{
    fun cardnumberformat(): String{
       return cardnumber!!.replace(" ", "")
    }
}