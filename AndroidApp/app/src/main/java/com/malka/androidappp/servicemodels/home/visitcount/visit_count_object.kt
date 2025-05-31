package com.malka.androidappp.servicemodels.home.visitcount

import com.malka.androidappp.botmnav_fragments.home.model.CarAdvertisment
import com.malka.androidappp.servicemodels.BaseModel

class visit_count_object(status_code: Int, message: String, val data: visit_count_properties) :
    BaseModel(status_code, message)
{
}