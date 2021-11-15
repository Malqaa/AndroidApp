package com.malka.androidappp.servicemodels.addtocart

import com.malka.androidappp.servicemodels.BaseModel

class AddToCartResponseModel(
    status_code: Int,
    message: String,
    val data: List<CartItemModel>
) : BaseModel(status_code, message)