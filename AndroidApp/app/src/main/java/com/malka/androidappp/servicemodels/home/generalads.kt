package com.malka.androidappp.servicemodels.home

import com.malka.androidappp.botmnav_fragments.home.model.Generaladvetisement
import com.malka.androidappp.servicemodels.BaseModel

class generalads(status_code: Int, message: String, val data: List<Generaladvetisement>) :
    BaseModel(status_code, message)
{
}