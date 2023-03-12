package com.malka.androidappp.newPhase.domain.models.servicemodels

class BasicResponse(
    status_code: Int,
    message: String,
    val data: Any

) : BaseModel(status_code, message)