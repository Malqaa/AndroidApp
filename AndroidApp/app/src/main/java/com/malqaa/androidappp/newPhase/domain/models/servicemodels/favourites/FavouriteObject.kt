package com.malqaa.androidappp.newPhase.domain.models.servicemodels.favourites

import com.malqaa.androidappp.newPhase.domain.models.servicemodels.BaseModel

class FavouriteObject(
    status_code: Int,
    message: String,
    val data: FavouriteListingObject

) : BaseModel(status_code, message)
