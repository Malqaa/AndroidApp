package com.malka.androidappp.design.Models

import com.malka.androidappp.servicemodels.creditcard.CreditCardModel

data class getCardDetailsModel(
    val `data`: List<CreditCardModel>,
    val message: String,
    val status_code: Int
)