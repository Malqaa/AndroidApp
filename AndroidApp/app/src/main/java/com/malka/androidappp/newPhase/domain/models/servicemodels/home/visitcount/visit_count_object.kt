package com.malka.androidappp.newPhase.domain.models.servicemodels.home.visitcount

import com.malka.androidappp.newPhase.domain.models.servicemodels.BaseModel

class visit_count_object(status_code: Int, message: String, val data: visit_count_properties) :
    BaseModel(status_code, message)
{
}