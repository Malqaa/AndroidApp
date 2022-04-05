package com.malka.androidappp.design.Models

data class getCardDetailsModel(
    val `data`: List<Data>,
    val message: String,
    val status_code: Int
) {
    data class Data(
        val cardType: String,
        val cardnumber: String,
        val createdOn: String,
        val createdby: String,
        val cvcNumber: Int,
        val expiryDate: String,
        val id: String,
        val isActive: Boolean,
        val updateby: Any,
        val updatedOn: String,
        val userId: String,
        val card_holder_name: String,
    )
}