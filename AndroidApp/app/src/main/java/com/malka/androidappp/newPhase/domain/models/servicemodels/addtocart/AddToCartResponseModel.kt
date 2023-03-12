package com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart

import com.malka.androidappp.newPhase.domain.models.servicemodels.BaseModel

class AddToCartResponseModel(
    status_code: Int,
    message: String,
    val data: List<CartItemModel>
) : BaseModel(status_code, message)