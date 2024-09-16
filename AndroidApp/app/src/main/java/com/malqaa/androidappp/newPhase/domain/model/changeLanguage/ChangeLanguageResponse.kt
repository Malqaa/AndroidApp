package com.casecode.domain.model.changeLanguage

import com.google.gson.annotations.SerializedName

data class ChangeLanguageResponse(
    val `data`: String,
    val message: String,
    @SerializedName("status_code")
    val statusCode: Int
)