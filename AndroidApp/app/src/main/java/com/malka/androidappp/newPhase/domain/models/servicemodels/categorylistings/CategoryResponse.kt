package com.malka.androidappp.newPhase.domain.models.servicemodels.categorylistings

import com.malka.androidappp.newPhase.domain.models.servicemodels.AdDetailModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.BaseModel
class CategoryResponse (
    status_code: Int,
    message: String,
    val data: List<AdDetailModel>

) : BaseModel(status_code, message)