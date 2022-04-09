package com.malka.androidappp.servicemodels

class BasicResponse(
    status_code: Int,
    message: String,
    val data: Any

) : BaseModel(status_code, message)