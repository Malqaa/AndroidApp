package com.malqaa.androidappp.newPhase.domain.models.servicemodels.user

import com.malqaa.androidappp.newPhase.domain.models.servicemodels.User
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.BaseModel

class UserObject(status_code: Int, message: String, val data: User) : BaseModel(
    status_code, message
) {
}