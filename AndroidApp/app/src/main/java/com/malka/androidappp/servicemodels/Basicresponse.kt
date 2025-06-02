package com.malka.androidappp.servicemodels

import com.malka.androidappp.servicemodels.BaseModel

class Basicresponse(
    status_code: Int,
    message: String,
    val data: Any

) : BaseModel(status_code, message)