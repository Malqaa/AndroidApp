package com.malka.androidappp.servicemodels.user

import com.malka.androidappp.servicemodels.BaseModel

class UserObject(status_code: Int, message: String, val data: UserProperties) : BaseModel(
    status_code, message
) {
}