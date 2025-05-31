package com.malka.androidappp.servicemodels.favourites

import com.malka.androidappp.servicemodels.BaseModel

class FavouriteObject(
    status_code: Int,
    message: String,
    val data: FavouriteListingObject

) : BaseModel(status_code, message)
