package com.malka.androidappp.newPhase.domain.models.servicemodels

class BasicResponseInt(
    status_code: Int,
    message: String,
    val data: Int

) : BaseModel(status_code, message)