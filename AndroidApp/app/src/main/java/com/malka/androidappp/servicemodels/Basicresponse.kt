package com.malka.androidappp.servicemodels

class Basicresponse(
    status_code: Int,
    message: String,
    val data: Any

) : BaseModel(status_code, message)