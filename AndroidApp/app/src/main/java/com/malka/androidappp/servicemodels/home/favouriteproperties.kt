package com.malka.androidappp.servicemodels.home

import com.malka.androidappp.botmnav_fragments.home.model.Propertyadvetisement
import com.malka.androidappp.servicemodels.BaseModel

class favouriteproperties(status_code: Int, message: String, val data: List<Propertyadvetisement>) :
    BaseModel(status_code, message)
{
}