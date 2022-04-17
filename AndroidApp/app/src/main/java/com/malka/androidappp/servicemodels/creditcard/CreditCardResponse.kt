package com.malka.androidappp.servicemodels.creditcard

import com.malka.androidappp.servicemodels.BaseModel

class CreditCardResponse (
    status_code: Int,
    message: String,
    val data: List<CreditCardModel>

) : BaseModel(status_code, message)