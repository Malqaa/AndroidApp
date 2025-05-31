package com.malka.androidappp.servicemodels

import com.malka.androidappp.servicemodels.BaseModel

class BasicResponseInt(
    status_code: Int,
    message: String,
    val data: Int

) : BaseModel(status_code, message)