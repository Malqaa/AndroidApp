package com.malka.androidappp.servicemodels.home

import com.malka.androidappp.botmnav_fragments.home.model.CarAdvertisment
import com.malka.androidappp.servicemodels.BaseModel

class favouritecars(status_code: Int, message: String, val data: List<CarAdvertisment>) :
    BaseModel(status_code, message)
{
}