package com.malka.androidappp.servicemodels.categorylistings

import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.BaseModel
class CategoryResponse (
    status_code: Int,
    message: String,
    val data: List<AdDetailModel>

) : BaseModel(status_code, message)