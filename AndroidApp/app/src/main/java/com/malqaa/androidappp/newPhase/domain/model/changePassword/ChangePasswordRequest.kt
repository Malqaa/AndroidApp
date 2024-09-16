package com.casecode.domain.model.changePassword

data class ChangePasswordRequest(
    val email: String,
    val resetPasswordCode: String,
    val newPassword: String
)
