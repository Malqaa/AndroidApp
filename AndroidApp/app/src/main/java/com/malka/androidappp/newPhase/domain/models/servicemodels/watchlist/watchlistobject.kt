package com.malka.androidappp.newPhase.domain.models.servicemodels.watchlist

import com.malka.androidappp.newPhase.domain.models.servicemodels.BaseModel

class watchlistobject(status_code: Int, message: String, val data: List<watchlistproperties>) :
    BaseModel(status_code, message)
{
}