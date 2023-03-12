package com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard

import com.malka.androidappp.newPhase.domain.models.servicemodels.BaseModel

class CreditCardResponse (
    status_code: Int,
    message: String,
    val data: List<CreditCardModel>

) : BaseModel(status_code, message)