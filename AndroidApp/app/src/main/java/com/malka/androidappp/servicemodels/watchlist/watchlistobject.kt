package com.malka.androidappp.servicemodels.watchlist

import com.malka.androidappp.servicemodels.BaseModel

class watchlistobject(status_code: Int, message: String, val data: List<watchlistproperties>) :
    BaseModel(status_code, message)
{
}