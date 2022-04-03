package com.malka.androidappp.servicemodels.user

import com.malka.androidappp.activities_main.signup_account.signup_pg3.User
import com.malka.androidappp.servicemodels.BaseModel

class UserObject(status_code: Int, message: String, val data: User) : BaseModel(
    status_code, message
) {
}