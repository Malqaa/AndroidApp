package com.malka.androidappp.servicemodels.home

import com.malka.androidappp.botmnav_fragments.home.model.Recentadvetisement
import com.malka.androidappp.servicemodels.BaseModel

class recentlisting(status_code: Int, message: String, val data: List<Recentadvetisement>) :
    BaseModel(status_code, message)
{
}