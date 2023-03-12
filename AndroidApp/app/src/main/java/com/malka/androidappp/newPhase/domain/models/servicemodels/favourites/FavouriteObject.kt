package com.malka.androidappp.newPhase.domain.models.servicemodels.favourites

import com.malka.androidappp.newPhase.domain.models.servicemodels.BaseModel

class FavouriteObject(
    status_code: Int,
    message: String,
    val data: FavouriteListingObject

) : BaseModel(status_code, message)
